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

class AtomicReadBlockingGCRingBuffer<T> implements EmptyRingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final Lock readLock = new Lock();

    private final Integer readPosition;
    private final Integer writePosition;

    AtomicReadBlockingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
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
    public void put(T element) {
        int writePosition = this.writePosition.getPlain();
        int newWritePosition;
        if (writePosition == 0) {
            newWritePosition = capacityMinusOne;
        } else {
            newWritePosition = writePosition - 1;
        }
        writeBusyWaitStrategy.reset();
        while (readPosition.get() == newWritePosition) {
            writeBusyWaitStrategy.tick();
        }
        buffer[writePosition] = element;
        this.writePosition.set(newWritePosition);
    }

    @Override
    public T take() {
        readLock.lock();
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
        T element = buffer[readPosition];
        buffer[readPosition] = null;
        readLock.unlock();
        return element;
    }

    @Override
    public void advance() {}

    @Override
    public void takeBatch(int size) {
        readLock.lock();
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
        T element = buffer[readPosition];
        buffer[readPosition] = null;
        return element;
    }

    @Override
    public void advanceBatch() {
        readLock.unlock();
    }

    @Override
    public void forEach(Consumer<T> action) {
        int writePosition = this.writePosition.get();
        readLock.lock();
        int readPosition = this.readPosition.getPlain();
        if (writePosition <= readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                action.accept(buffer[readPosition]);
            }
        } else {
            forEachSplit(action, readPosition, writePosition);
        }
        readLock.unlock();
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
        int writePosition = this.writePosition.get();
        readLock.lock();
        int readPosition = this.readPosition.getPlain();
        try {
            if (writePosition <= readPosition) {
                for (; readPosition > writePosition; readPosition--) {
                    if (buffer[readPosition].equals(element)) {
                        return true;
                    }
                }
                return false;
            }
            return containsSplit(element, readPosition, writePosition);
        } finally {
            readLock.unlock();
        }
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

    private static boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int writePosition = this.writePosition.get();
        readLock.lock();
        int readPosition = this.readPosition.getPlain();
        if (isEmpty(readPosition, writePosition)) {
            readLock.unlock();
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
        readLock.unlock();
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
