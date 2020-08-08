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

abstract class AtomicWriteHeapRingBuffer_pad0 {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class AtomicWriteHeapRingBuffer_buf extends AtomicWriteHeapRingBuffer_pad0 {
    final int capacity;
    final int capacityMinusOne;
    final byte[] buffer;
    final Lock writeLock;
    final BusyWaitStrategy readBusyWaitStrategy;
    final IntHandle writePositionHandle;

    AtomicWriteHeapRingBuffer_buf(HeapClearingRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writeLock = builder.getWriteLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePositionHandle = builder.newHandle();
    }
}

abstract class AtomicWriteHeapRingBuffer_pad1 extends AtomicWriteHeapRingBuffer_buf {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicWriteHeapRingBuffer_pad1(HeapClearingRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class AtomicWriteHeapRingBuffer_read extends AtomicWriteHeapRingBuffer_pad1 {
    int readPosition;
    int cachedWritePosition;

    AtomicWriteHeapRingBuffer_read(HeapClearingRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class AtomicWriteHeapRingBuffer_pad2 extends AtomicWriteHeapRingBuffer_read {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicWriteHeapRingBuffer_pad2(HeapClearingRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class AtomicWriteHeapRingBuffer_write extends AtomicWriteHeapRingBuffer_pad2 {
    int writePosition;

    AtomicWriteHeapRingBuffer_write(HeapClearingRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class AtomicWriteHeapRingBuffer_pad3 extends AtomicWriteHeapRingBuffer_write {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicWriteHeapRingBuffer_pad3(HeapClearingRingBufferBuilder builder) {
        super(builder);
    }
}

class AtomicWriteHeapRingBuffer extends AtomicWriteHeapRingBuffer_pad3 implements HeapClearingRingBuffer {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(AtomicWriteHeapRingBuffer_write.class, "writePosition");

    AtomicWriteHeapRingBuffer(HeapClearingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int next() {
        writeLock.lock();
        return writePosition;
    }

    @Override
    public void put(int offset) {
        writePositionHandle.set(this, WRITE_POSITION, offset);
        writeLock.unlock();
    }

    @Override
    public int take(int size) {
        int readPosition = this.readPosition & capacityMinusOne;
        readBusyWaitStrategy.reset();
        while (isNotFullEnoughCached(readPosition, size)) {
            readBusyWaitStrategy.tick();
        }
        readPosition = this.readPosition;
        this.readPosition += size;
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
    public void advance() {
    }

    @Override
    public int size() {
        return size(readPosition & capacityMinusOne, writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne);
    }

    private int size(int readPosition, int writePosition) {
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return (writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne) == (readPosition & capacityMinusOne);
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
