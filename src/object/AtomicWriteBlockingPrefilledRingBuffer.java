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

import org.ringbuffer.Lock;
import org.ringbuffer.memory.Integer;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

class AtomicWriteBlockingPrefilledRingBuffer<T> implements PrefilledRingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final Lock writeLock = new Lock();

    private final Integer readPosition;
    private final Integer writePosition;

    AtomicWriteBlockingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
        readPosition = builder.newCursor();
        writePosition = builder.newCursor();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int nextKey() {
        writeLock.lock();
        return writePosition.getPlain();
    }

    @Override
    public int nextPutKey(int key) {
        if (key == 0) {
            return capacityMinusOne;
        }
        return key - 1;
    }

    @Override
    public T next(int key, int putKey) {
        writeBusyWaitStrategy.reset();
        while (readPosition.get() == putKey) {
            writeBusyWaitStrategy.tick();
        }
        return buffer[key];
    }

    @Override
    public void put(int putKey) {
        writePosition.set(putKey);
        writeLock.unlock();
    }

    @Override
    public T take() {
        int readPosition = this.readPosition.getPlain();
        readBusyWaitStrategy.reset();
        while (writePosition.get() == readPosition) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == 0) {
            this.readPosition.set(capacityMinusOne);
        } else {
            this.readPosition.set(readPosition - 1);
        }
        return buffer[readPosition];
    }

    @Override
    public void advance() {}

    @Override
    public void takeBatch(int size) {
        int readPosition = this.readPosition.getPlain();
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
            readBusyWaitStrategy.tick();
        }
    }

    @Override
    public T takePlain() {
        int readPosition = this.readPosition.getPlain();
        if (readPosition == 0) {
            this.readPosition.set(capacityMinusOne);
        } else {
            this.readPosition.set(readPosition - 1);
        }
        return buffer[readPosition];
    }

    @Override
    public void advanceBatch() {}

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                action.accept(buffer[readPosition]);
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
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                if (buffer[readPosition].equals(element)) {
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
        return size(readPosition.get());
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
        return isEmpty(readPosition.get(), writePosition.get());
    }

    private boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (isEmpty(readPosition, writePosition)) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        if (writePosition < readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                builder.append(buffer[readPosition].toString());
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
}
