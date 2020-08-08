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

abstract class AtomicWriteDiscardingPrefilledRingBuffer_pad0 {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class AtomicWriteDiscardingPrefilledRingBuffer_buf<T> extends AtomicWriteDiscardingPrefilledRingBuffer_pad0 {
    final int capacity;
    final int capacityMinusOne;
    final T[] buffer;
    final Lock writeLock;
    final BusyWaitStrategy readBusyWaitStrategy;
    final T dummyElement;
    final IntHandle readPositionHandle;
    final IntHandle writePositionHandle;

    AtomicWriteDiscardingPrefilledRingBuffer_buf(PrefilledRingBufferBuilder2<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writeLock = builder.getWriteLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        dummyElement = builder.getDummyElement();
        readPositionHandle = builder.newHandle();
        writePositionHandle = builder.newHandle();
    }
}

abstract class AtomicWriteDiscardingPrefilledRingBuffer_pad1<T> extends AtomicWriteDiscardingPrefilledRingBuffer_buf<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicWriteDiscardingPrefilledRingBuffer_pad1(PrefilledRingBufferBuilder2<T> builder) {
        super(builder);
    }
}

abstract class AtomicWriteDiscardingPrefilledRingBuffer_read<T> extends AtomicWriteDiscardingPrefilledRingBuffer_pad1<T> {
    int readPosition;
    int cachedWritePosition;

    AtomicWriteDiscardingPrefilledRingBuffer_read(PrefilledRingBufferBuilder2<T> builder) {
        super(builder);
    }
}

abstract class AtomicWriteDiscardingPrefilledRingBuffer_pad2<T> extends AtomicWriteDiscardingPrefilledRingBuffer_read<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicWriteDiscardingPrefilledRingBuffer_pad2(PrefilledRingBufferBuilder2<T> builder) {
        super(builder);
    }
}

abstract class AtomicWriteDiscardingPrefilledRingBuffer_write<T> extends AtomicWriteDiscardingPrefilledRingBuffer_pad2<T> {
    int writePosition;
    int cachedReadPosition;

    AtomicWriteDiscardingPrefilledRingBuffer_write(PrefilledRingBufferBuilder2<T> builder) {
        super(builder);
    }
}

abstract class AtomicWriteDiscardingPrefilledRingBuffer_pad3<T> extends AtomicWriteDiscardingPrefilledRingBuffer_write<T> {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicWriteDiscardingPrefilledRingBuffer_pad3(PrefilledRingBufferBuilder2<T> builder) {
        super(builder);
    }
}

class AtomicWriteDiscardingPrefilledRingBuffer<T> extends AtomicWriteDiscardingPrefilledRingBuffer_pad3<T> implements PrefilledRingBuffer2<T> {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        READ_POSITION = Unsafe.objectFieldOffset(AtomicWriteDiscardingPrefilledRingBuffer_read.class, "readPosition");
        WRITE_POSITION = Unsafe.objectFieldOffset(AtomicWriteDiscardingPrefilledRingBuffer_write.class, "writePosition");
    }

    AtomicWriteDiscardingPrefilledRingBuffer(PrefilledRingBufferBuilder2<T> builder) {
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
    public int nextPutKey(int key) {
        if (key == 0) {
            return capacityMinusOne;
        }
        return key - 1;
    }

    @Override
    public T next(int key, int putKey) {
        if (isNotFullCached(putKey)) {
            return AtomicArray.getPlain(buffer, key);
        }
        return dummyElement;
    }

    private boolean isNotFullCached(int writePosition) {
        if (cachedReadPosition == writePosition) {
            cachedReadPosition = readPositionHandle.get(this, READ_POSITION);
            return cachedReadPosition != writePosition;
        }
        return true;
    }

    @Override
    public void put(int putKey) {
        writePositionHandle.set(this, WRITE_POSITION, putKey);
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
            readPositionHandle.set(this, READ_POSITION, capacityMinusOne);
        } else {
            readPositionHandle.set(this, READ_POSITION, readPosition - 1);
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
            readPositionHandle.set(this, READ_POSITION, capacityMinusOne);
        } else {
            readPositionHandle.set(this, READ_POSITION, readPosition - 1);
        }
        return AtomicArray.getPlain(buffer, readPosition);
    }

    @Override
    public void advanceBatch() {
    }

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = readPositionHandle.get(this, READ_POSITION);
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (writePosition <= readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                action.accept(AtomicArray.getPlain(buffer, readPosition));
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
        int readPosition = readPositionHandle.get(this, READ_POSITION);
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
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
        return size(readPositionHandle.get(this, READ_POSITION));
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
        return isEmpty(readPositionHandle.get(this, READ_POSITION), writePositionHandle.get(this, WRITE_POSITION));
    }

    private static boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int readPosition = readPositionHandle.get(this, READ_POSITION);
        int writePosition = writePositionHandle.get(this, WRITE_POSITION);
        if (isEmpty(readPosition, writePosition)) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        if (writePosition < readPosition) {
            for (; readPosition > writePosition; readPosition--) {
                builder.append(AtomicArray.getPlain(buffer, readPosition).toString());
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
}
