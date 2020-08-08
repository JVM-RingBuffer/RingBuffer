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

import org.ringbuffer.concurrent.AtomicArray;
import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.IntHandle;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

abstract class AtomicReadGCRingBuffer_pad0 {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class AtomicReadGCRingBuffer_buf<T> extends AtomicReadGCRingBuffer_pad0 {
    final int capacity;
    final int capacityMinusOne;
    final T[] buffer;
    final Lock readLock;
    final BusyWaitStrategy readBusyWaitStrategy;
    final IntHandle writePositionHandle;

    AtomicReadGCRingBuffer_buf(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readLock = builder.getReadLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePositionHandle = builder.newHandle();
    }
}

abstract class AtomicReadGCRingBuffer_pad1<T> extends AtomicReadGCRingBuffer_buf<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicReadGCRingBuffer_pad1(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class AtomicReadGCRingBuffer_read<T> extends AtomicReadGCRingBuffer_pad1<T> {
    int readPosition;
    int cachedWritePosition;

    AtomicReadGCRingBuffer_read(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class AtomicReadGCRingBuffer_pad2<T> extends AtomicReadGCRingBuffer_read<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicReadGCRingBuffer_pad2(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class AtomicReadGCRingBuffer_write<T> extends AtomicReadGCRingBuffer_pad2<T> {
    int writePosition;

    AtomicReadGCRingBuffer_write(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class AtomicReadGCRingBuffer_pad3<T> extends AtomicReadGCRingBuffer_write<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicReadGCRingBuffer_pad3(RingBufferBuilder<T> builder) {
        super(builder);
    }
}

class AtomicReadGCRingBuffer<T> extends AtomicReadGCRingBuffer_pad3<T> implements RingBuffer<T> {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(AtomicReadGCRingBuffer_write.class, "writePosition");

    AtomicReadGCRingBuffer(RingBufferBuilder<T> builder) {
        super(builder);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void put(T element) {
        int writePosition = this.writePosition;
        AtomicArray.setPlain(buffer, writePosition, element);
        if (writePosition == 0) {
            writePositionHandle.set(this, WRITE_POSITION, capacityMinusOne);
        } else {
            writePositionHandle.set(this, WRITE_POSITION, writePosition - 1);
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
        T element = AtomicArray.getPlain(buffer, readPosition);
        AtomicArray.setPlain(buffer, readPosition, null);
        return element;
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
        readLock.lock();
        int readPosition = this.readPosition;
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
            readBusyWaitStrategy.tick();
        }
    }

    @Override
    public T takePlain() {
        T element = AtomicArray.getPlain(buffer, readPosition);
        AtomicArray.setPlain(buffer, readPosition, null);
        if (readPosition == 0) {
            readPosition = capacityMinusOne;
        } else {
            readPosition--;
        }
        return element;
    }

    @Override
    public void advanceBatch() {
        readLock.unlock();
    }

    @Override
    public void forEach(Consumer<T> action) {
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        readLock.lock();
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(AtomicArray.getPlain(buffer, i));
            }
        } else {
            forEachSplit(action, writePosition);
        }
        readLock.unlock();
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
        readLock.lock();
        try {
            if (writePosition <= readPosition) {
                for (int i = readPosition; i > writePosition; i--) {
                    if (AtomicArray.getPlain(buffer, i).equals(element)) {
                        return true;
                    }
                }
                return false;
            }
            return containsSplit(element, writePosition);
        } finally {
            readLock.unlock();
        }
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
        return size(getReadPosition());
    }

    private int size(int readPosition) {
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(getReadPosition(), writePositionHandle.get(this, WRITE_POSITION));
    }

    private static boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        readLock.lock();
        if (isEmpty(readPosition, writePosition)) {
            readLock.unlock();
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
        readLock.unlock();
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

    private int getReadPosition() {
        readLock.lock();
        int readPosition = this.readPosition;
        readLock.unlock();
        return readPosition;
    }
}
