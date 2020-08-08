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

abstract class ConcurrentPrefilledRingBuffer_pad0 {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class ConcurrentPrefilledRingBuffer_buf<T> extends ConcurrentPrefilledRingBuffer_pad0 {
    final int capacity;
    final int capacityMinusOne;
    final T[] buffer;
    final Lock readLock;
    final Lock writeLock;
    final BusyWaitStrategy readBusyWaitStrategy;
    final IntHandle writePositionHandle;

    ConcurrentPrefilledRingBuffer_buf(PrefilledRingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readLock = builder.getReadLock();
        writeLock = builder.getWriteLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePositionHandle = builder.newHandle();
    }
}

abstract class ConcurrentPrefilledRingBuffer_pad1<T> extends ConcurrentPrefilledRingBuffer_buf<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    ConcurrentPrefilledRingBuffer_pad1(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class ConcurrentPrefilledRingBuffer_read<T> extends ConcurrentPrefilledRingBuffer_pad1<T> {
    int readPosition;
    int cachedWritePosition;

    ConcurrentPrefilledRingBuffer_read(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class ConcurrentPrefilledRingBuffer_pad2<T> extends ConcurrentPrefilledRingBuffer_read<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    ConcurrentPrefilledRingBuffer_pad2(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class ConcurrentPrefilledRingBuffer_write<T> extends ConcurrentPrefilledRingBuffer_pad2<T> {
    int writePosition;

    ConcurrentPrefilledRingBuffer_write(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

abstract class ConcurrentPrefilledRingBuffer_pad3<T> extends ConcurrentPrefilledRingBuffer_write<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    ConcurrentPrefilledRingBuffer_pad3(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }
}

class ConcurrentPrefilledRingBuffer<T> extends ConcurrentPrefilledRingBuffer_pad3<T> implements PrefilledRingBuffer<T> {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(ConcurrentPrefilledRingBuffer_write.class, "writePosition");

    ConcurrentPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int nextKey() {
        writeLock.lock();
        return writePosition;
    }

    @Override
    public T next(int key) {
        return AtomicArray.getPlain(buffer, key);
    }

    @Override
    public void put(int key) {
        if (key == 0) {
            writePositionHandle.set(this, WRITE_POSITION, capacityMinusOne);
        } else {
            writePositionHandle.set(this, WRITE_POSITION, key - 1);
        }
        writeLock.unlock();
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
        return AtomicArray.getPlain(buffer, readPosition);
    }

    @Override
    public void advanceBatch() {
        readLock.unlock();
    }

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = getReadPosition();
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(AtomicArray.getPlain(buffer, i));
            }
        } else {
            forEachSplit(action, readPosition, writePosition);
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
        int readPosition = getReadPosition();
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (AtomicArray.getPlain(buffer, i).equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, readPosition, writePosition);
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
        int readPosition = getReadPosition();
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (isEmpty(readPosition, writePosition)) {
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
            toStringSplit(builder, readPosition, writePosition);
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

    private int getReadPosition() {
        readLock.lock();
        int readPosition = this.readPosition;
        readLock.unlock();
        return readPosition;
    }
}
