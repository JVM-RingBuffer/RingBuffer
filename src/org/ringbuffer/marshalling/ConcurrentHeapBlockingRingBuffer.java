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

package org.ringbuffer.marshalling;

import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.IntHandle;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;

import static org.ringbuffer.marshalling.HeapBuffer.*;

abstract class ConcurrentHeapBlockingRingBuffer_pad0 {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class ConcurrentHeapBlockingRingBuffer_buf extends ConcurrentHeapBlockingRingBuffer_pad0 {
    final int capacity;
    final int capacityMinusOne;
    final byte[] buffer;
    final Lock readLock;
    final Lock writeLock;
    final BusyWaitStrategy readBusyWaitStrategy;
    final BusyWaitStrategy writeBusyWaitStrategy;
    final IntHandle readPositionHandle;
    final IntHandle writePositionHandle;

    ConcurrentHeapBlockingRingBuffer_buf(HeapRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readLock = builder.getReadLock();
        writeLock = builder.getWriteLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
        readPositionHandle = builder.newHandle();
        writePositionHandle = builder.newHandle();
    }
}

abstract class ConcurrentHeapBlockingRingBuffer_pad1 extends ConcurrentHeapBlockingRingBuffer_buf {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    ConcurrentHeapBlockingRingBuffer_pad1(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class ConcurrentHeapBlockingRingBuffer_read extends ConcurrentHeapBlockingRingBuffer_pad1 {
    int readPosition;
    int cachedWritePosition;

    ConcurrentHeapBlockingRingBuffer_read(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class ConcurrentHeapBlockingRingBuffer_pad2 extends ConcurrentHeapBlockingRingBuffer_read {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    ConcurrentHeapBlockingRingBuffer_pad2(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class ConcurrentHeapBlockingRingBuffer_write extends ConcurrentHeapBlockingRingBuffer_pad2 {
    int writePosition;
    int cachedReadPosition;

    ConcurrentHeapBlockingRingBuffer_write(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class ConcurrentHeapBlockingRingBuffer_pad3 extends ConcurrentHeapBlockingRingBuffer_write {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    ConcurrentHeapBlockingRingBuffer_pad3(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

class ConcurrentHeapBlockingRingBuffer extends ConcurrentHeapBlockingRingBuffer_pad3 implements HeapRingBuffer {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        READ_POSITION = Unsafe.objectFieldOffset(ConcurrentHeapBlockingRingBuffer_read.class, "readPosition");
        WRITE_POSITION = Unsafe.objectFieldOffset(ConcurrentHeapBlockingRingBuffer_write.class, "writePosition");
    }

    ConcurrentHeapBlockingRingBuffer(HeapRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int next(int size) {
        writeLock.lock();
        int writePosition = this.writePosition & capacityMinusOne;
        writeBusyWaitStrategy.reset();
        while (isNotEmptyEnoughCached(writePosition, size)) {
            writeBusyWaitStrategy.tick();
        }
        return writePosition;
    }

    private boolean isNotEmptyEnoughCached(int writePosition, int size) {
        if (freeSpace(writePosition, cachedReadPosition) <= size) {
            cachedReadPosition = readPositionHandle.get(this, READ_POSITION) & capacityMinusOne;
            return freeSpace(writePosition, cachedReadPosition) <= size;
        }
        return false;
    }

    private int freeSpace(int writePosition, int readPosition) {
        if (writePosition >= readPosition) {
            return capacity - (writePosition - readPosition);
        }
        return readPosition - writePosition;
    }

    @Override
    public void put(int offset) {
        writePositionHandle.set(this, WRITE_POSITION, offset);
        writeLock.unlock();
    }

    @Override
    public int take(int size) {
        readLock.lock();
        int readPosition = this.readPosition & capacityMinusOne;
        readBusyWaitStrategy.reset();
        while (isNotFullEnoughCached(readPosition, size)) {
            readBusyWaitStrategy.tick();
        }
        return readPosition;
    }

    private boolean isNotFullEnoughCached(int readPosition, int size) {
        if (size(readPosition, cachedWritePosition) < size) {
            cachedWritePosition = writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne;
            return size(readPosition, cachedWritePosition) < size;
        }
        return false;
    }

    @Override
    public void advance(int offset) {
        readPositionHandle.set(this, READ_POSITION, offset);
        readLock.unlock();
    }

    @Override
    public int size() {
        return size(readPositionHandle.get(this, READ_POSITION) & capacityMinusOne, writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne);
    }

    private int size(int readPosition, int writePosition) {
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return (writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne) == (readPositionHandle.get(this, READ_POSITION) & capacityMinusOne);
    }

    @Override
    public void writeByte(int offset, byte value) {
        putByte(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeChar(int offset, char value) {
        putChar(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeShort(int offset, short value) {
        putShort(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeInt(int offset, int value) {
        putInt(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeLong(int offset, long value) {
        putLong(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeBoolean(int offset, boolean value) {
        putBoolean(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeFloat(int offset, float value) {
        putFloat(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeDouble(int offset, double value) {
        putDouble(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public byte readByte(int offset) {
        return getByte(buffer, offset & capacityMinusOne);
    }

    @Override
    public char readChar(int offset) {
        return getChar(buffer, offset & capacityMinusOne);
    }

    @Override
    public short readShort(int offset) {
        return getShort(buffer, offset & capacityMinusOne);
    }

    @Override
    public int readInt(int offset) {
        return getInt(buffer, offset & capacityMinusOne);
    }

    @Override
    public long readLong(int offset) {
        return getLong(buffer, offset & capacityMinusOne);
    }

    @Override
    public boolean readBoolean(int offset) {
        return getBoolean(buffer, offset & capacityMinusOne);
    }

    @Override
    public float readFloat(int offset) {
        return getFloat(buffer, offset & capacityMinusOne);
    }

    @Override
    public double readDouble(int offset) {
        return getDouble(buffer, offset & capacityMinusOne);
    }
}
