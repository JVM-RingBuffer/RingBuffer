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
import org.ringbuffer.concurrent.AtomicInt;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

@Contended
class AtomicReadDiscardingGCRingBuffer<T> implements RingBuffer<T> {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        final Class<?> clazz = AtomicReadDiscardingGCRingBuffer.class;
        READ_POSITION = Unsafe.objectFieldOffset(clazz, "readPosition");
        WRITE_POSITION = Unsafe.objectFieldOffset(clazz, "writePosition");
    }

    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended("read")
    private int readPosition;
    @Contended("write")
    private int writePosition;
    @Contended("write")
    private int cachedReadPosition;
    @Contended("read")
    private int cachedWritePosition;

    AtomicReadDiscardingGCRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void put(T element) {
        int writePosition = this.writePosition;
        int newWritePosition;
        if (writePosition == 0) {
            newWritePosition = capacityMinusOne;
        } else {
            newWritePosition = writePosition - 1;
        }
        if (isNotFullCached(newWritePosition)) {
            AtomicArray.setPlain(buffer, writePosition, element);
            AtomicInt.setRelease(this, WRITE_POSITION, newWritePosition);
        }
    }

    private boolean isNotFullCached(int writePosition) {
        if (cachedReadPosition == writePosition) {
            cachedReadPosition = AtomicInt.getAcquire(this, READ_POSITION);
            return cachedReadPosition != writePosition;
        }
        return true;
    }

    @Override
    public synchronized T take() {
        int readPosition = this.readPosition;
        readBusyWaitStrategy.reset();
        while (isEmptyCached(readPosition)) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == 0) {
            AtomicInt.setRelease(this, READ_POSITION, capacityMinusOne);
        } else {
            AtomicInt.setRelease(this, READ_POSITION, readPosition - 1);
        }
        T element = AtomicArray.getPlain(buffer, readPosition);
        AtomicArray.setPlain(buffer, readPosition, null);
        return element;
    }

    private boolean isEmptyCached(int readPosition) {
        if (cachedWritePosition == readPosition) {
            cachedWritePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
            return cachedWritePosition == readPosition;
        }
        return false;
    }

    @Override
    public Object getReadMonitor() {
        return this;
    }

    @Override
    public void takeBatch(int size) {
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
            AtomicInt.setRelease(this, READ_POSITION, capacityMinusOne);
        } else {
            AtomicInt.setRelease(this, READ_POSITION, readPosition - 1);
        }
        T element = AtomicArray.getPlain(buffer, readPosition);
        AtomicArray.setPlain(buffer, readPosition, null);
        return element;
    }

    @Override
    public void forEach(Consumer<T> action) {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        synchronized (this) {
            int readPosition = this.readPosition;
            if (writePosition <= readPosition) {
                for (; readPosition > writePosition; readPosition--) {
                    action.accept(AtomicArray.getPlain(buffer, readPosition));
                }
            } else {
                forEachSplit(action, readPosition, writePosition);
            }
        }
    }

    private void forEachSplit(Consumer<T> action, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            action.accept(AtomicArray.getPlain(buffer, readPosition));
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            action.accept(AtomicArray.getPlain(buffer, readPosition));
        }
    }

    @Override
    public boolean contains(T element) {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        synchronized (this) {
            int readPosition = this.readPosition;
            if (writePosition <= readPosition) {
                for (; readPosition > writePosition; readPosition--) {
                    if (AtomicArray.getPlain(buffer, readPosition).equals(element)) {
                        return true;
                    }
                }
                return false;
            }
            return containsSplit(element, readPosition, writePosition);
        }
    }

    private boolean containsSplit(T element, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            if (AtomicArray.getPlain(buffer, readPosition).equals(element)) {
                return true;
            }
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            if (AtomicArray.getPlain(buffer, readPosition).equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size(AtomicInt.getAcquire(this, READ_POSITION));
    }

    private int size(int readPosition) {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(AtomicInt.getAcquire(this, READ_POSITION), AtomicInt.getAcquire(this, WRITE_POSITION));
    }

    private static boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int writePosition = AtomicInt.getAcquire(this, WRITE_POSITION);
        StringBuilder builder;
        synchronized (this) {
            int readPosition = this.readPosition;
            if (isEmpty(readPosition, writePosition)) {
                return "[]";
            }
            builder = new StringBuilder();
            builder.append('[');
            if (writePosition < readPosition) {
                for (; readPosition > writePosition; readPosition--) {
                    builder.append(AtomicArray.getPlain(buffer, readPosition).toString());
                    builder.append(", ");
                }
            } else {
                toStringSplit(builder, readPosition, writePosition);
            }
        }
        builder.setLength(builder.length() - 2);
        builder.append(']');
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder, int readPosition, int writePosition) {
        for (; readPosition >= 0; readPosition--) {
            builder.append(AtomicArray.getPlain(buffer, readPosition).toString());
            builder.append(", ");
        }
        for (readPosition = capacityMinusOne; readPosition > writePosition; readPosition--) {
            builder.append(AtomicArray.getPlain(buffer, readPosition).toString());
            builder.append(", ");
        }
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        throw new UnsupportedOperationException();
    }
}
