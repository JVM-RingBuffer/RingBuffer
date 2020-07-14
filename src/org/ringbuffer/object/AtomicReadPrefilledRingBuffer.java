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
import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.Integer;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

class AtomicReadPrefilledRingBuffer<T> implements PrefilledRingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final Lock readLock;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended("read")
    private int readPosition;
    private final Integer writePosition;
    @Contended("read")
    private int cachedWritePosition;

    AtomicReadPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readLock = builder.getReadLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePosition = builder.newCursor();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int nextKey() {
        return writePosition.getPlain();
    }

    @Override
    public T next(int key) {
        return buffer[key];
    }

    @Override
    public void put(int key) {
        if (key == 0) {
            writePosition.set(capacityMinusOne);
        } else {
            writePosition.set(key - 1);
        }
    }

    @Override
    public T take() {
        readLock.lock();
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
        readLock.unlock();
        return buffer[readPosition];
    }

    private boolean isEmptyCached(int readPosition) {
        if (cachedWritePosition == readPosition) {
            cachedWritePosition = writePosition.get();
            return cachedWritePosition == readPosition;
        }
        return false;
    }

    @Override
    public void advance() {}

    @Override
    public void takeBatch(int size) {
        readLock.lock();
        int readPosition = this.readPosition;
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
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
        return buffer[readPosition];
    }

    @Override
    public void advanceBatch() {
        readLock.unlock();
    }

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = getReadPosition();
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(buffer[i]);
            }
        } else {
            forEachSplit(action, readPosition, writePosition);
        }
    }

    private void forEachSplit(Consumer<T> action, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            action.accept(buffer[readPosition]);
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            action.accept(buffer[readPosition]);
        }
    }

    @Override
    public boolean contains(T element) {
        int readPosition = getReadPosition();
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (buffer[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, readPosition, writePosition);
    }

    private boolean containsSplit(T element, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            if (buffer[readPosition].equals(element)) {
                return true;
            }
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            if (buffer[readPosition].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size(getReadPosition());
    }

    private int size(int readPosition) {
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(getReadPosition(), writePosition.get());
    }

    private static boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int readPosition = getReadPosition();
        int writePosition = this.writePosition.get();
        if (isEmpty(readPosition, writePosition)) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        if (writePosition < readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                builder.append(buffer[i].toString());
                builder.append(", ");
            }
        } else {
            toStringSplit(builder, readPosition, writePosition);
        }
        builder.setLength(builder.length() - 2);
        builder.append(']');
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            builder.append(buffer[readPosition].toString());
            builder.append(", ");
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            builder.append(buffer[readPosition].toString());
            builder.append(", ");
        }
    }

    private int getReadPosition() {
        readLock.lock();
        int readPosition = this.readPosition;
        readLock.unlock();
        return readPosition;
    }
}
