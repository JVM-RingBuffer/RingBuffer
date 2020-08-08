/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ringbuffer.object;

import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.concurrent.AtomicArray;
import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.IntHandle;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

@Contended
class AtomicWriteRingBuffer<T> implements RingBuffer<T> {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(AtomicWriteRingBuffer.class, "writePosition");

    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final Lock writeLock;
    private final BusyWaitStrategy readBusyWaitStrategy;

    private final IntHandle writePositionHandle;
    @Contended("read")
    private int readPosition;
    @Contended
    private int writePosition;
    @Contended("read")
    private int cachedWritePosition;

    AtomicWriteRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writeLock = builder.getWriteLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePositionHandle = builder.newHandle();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void put(T element) {
        writeLock.lock();
        int writePosition = this.writePosition;
        AtomicArray.setPlain(buffer, writePosition, element);
        if (writePosition == 0) {
            writePositionHandle.set(this, WRITE_POSITION, capacityMinusOne);
        } else {
            writePositionHandle.set(this, WRITE_POSITION, writePosition - 1);
        }
        writeLock.unlock();
    }

    @Override
    public T take() {
        int readPosition = this.readPosition;
        readBusyWaitStrategy.reset();
        while (isEmptyCached(readPosition)) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == 0) {
            this.readPosition = capacityMinusOne;
        } else {
            this.readPosition--;
        }
        return AtomicArray.getPlain(buffer, readPosition);
    }

    private boolean isEmptyCached(int readPosition) {
        if (cachedWritePosition == readPosition) {
            cachedWritePosition = writePositionHandle.get(this, WRITE_POSITION);
            return cachedWritePosition == readPosition;
        }
        return false;
    }

    @Override
    public void advance() {
    }

    @Override
    public void takeBatch(int size) {
        readBusyWaitStrategy.reset();
        while (size() < size) {
            readBusyWaitStrategy.tick();
        }
    }

    @Override
    public T takePlain() {
        int readPosition = this.readPosition;
        if (readPosition == 0) {
            this.readPosition = capacityMinusOne;
        } else {
            this.readPosition--;
        }
        return AtomicArray.getPlain(buffer, readPosition);
    }

    @Override
    public void advanceBatch() {
    }

    @Override
    public void forEach(Consumer<T> action) {
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(AtomicArray.getPlain(buffer, i));
            }
        } else {
            forEachSplit(action, writePosition);
        }
    }

    private void forEachSplit(Consumer<T> action, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            action.accept(AtomicArray.getPlain(buffer, i));
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            action.accept(AtomicArray.getPlain(buffer, i));
        }
    }

    @Override
    public boolean contains(T element) {
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (AtomicArray.getPlain(buffer, i).equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, writePosition);
    }

    private boolean containsSplit(T element, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            if (AtomicArray.getPlain(buffer, i).equals(element)) {
                return true;
            }
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            if (AtomicArray.getPlain(buffer, i).equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(writePositionHandle.get(this, WRITE_POSITION));
    }

    private boolean isEmpty(int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (isEmpty(writePosition)) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        if (writePosition < readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                builder.append(AtomicArray.getPlain(buffer, i).toString());
                builder.append(", ");
            }
        } else {
            toStringSplit(builder, writePosition);
        }
        builder.setLength(builder.length() - 2);
        builder.append(']');
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            builder.append(AtomicArray.getPlain(buffer, i).toString());
            builder.append(", ");
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            builder.append(AtomicArray.getPlain(buffer, i).toString());
            builder.append(", ");
        }
    }
}
