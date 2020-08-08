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
import org.ringbuffer.memory.LongHandle;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;

import static org.ringbuffer.marshalling.DirectBuffer.*;

abstract class AtomicReadDirectRingBuffer_pad0 {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class AtomicReadDirectRingBuffer_buf extends AtomicReadDirectRingBuffer_pad0 {
    final long capacity;
    final long capacityMinusOne;
    final long buffer;
    final Lock readLock;
    final BusyWaitStrategy readBusyWaitStrategy;
    final LongHandle writePositionHandle;

    AtomicReadDirectRingBuffer_buf(DirectClearingRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readLock = builder.getReadLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePositionHandle = builder.newHandle();
    }
}

abstract class AtomicReadDirectRingBuffer_pad1 extends AtomicReadDirectRingBuffer_buf {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicReadDirectRingBuffer_pad1(DirectClearingRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class AtomicReadDirectRingBuffer_read extends AtomicReadDirectRingBuffer_pad1 {
    long readPosition;
    long cachedWritePosition;

    AtomicReadDirectRingBuffer_read(DirectClearingRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class AtomicReadDirectRingBuffer_pad2 extends AtomicReadDirectRingBuffer_read {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicReadDirectRingBuffer_pad2(DirectClearingRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class AtomicReadDirectRingBuffer_write extends AtomicReadDirectRingBuffer_pad2 {
    long writePosition;

    AtomicReadDirectRingBuffer_write(DirectClearingRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class AtomicReadDirectRingBuffer_pad3 extends AtomicReadDirectRingBuffer_write {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    AtomicReadDirectRingBuffer_pad3(DirectClearingRingBufferBuilder builder) {
        super(builder);
    }
}

class AtomicReadDirectRingBuffer extends AtomicReadDirectRingBuffer_pad3 implements DirectClearingRingBuffer {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(AtomicReadDirectRingBuffer_write.class, "writePosition");

    AtomicReadDirectRingBuffer(DirectClearingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long next() {
        return writePosition;
    }

    @Override
    public void put(long offset) {
        writePositionHandle.set(this, WRITE_POSITION, offset);
    }

    @Override
    public long take(long size) {
        readLock.lock();
        long readPosition = this.readPosition & capacityMinusOne;
        readBusyWaitStrategy.reset();
        while (isNotFullEnoughCached(readPosition, size)) {
            readBusyWaitStrategy.tick();
        }
        readPosition = this.readPosition;
        this.readPosition += size;
        return readPosition;
    }

    private boolean isNotFullEnoughCached(long readPosition, long size) {
        if (size(readPosition, cachedWritePosition) < size) {
            cachedWritePosition = writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne;
            return size(readPosition, cachedWritePosition) < size;
        }
        return false;
    }

    @Override
    public void advance() {
        readLock.unlock();
    }

    @Override
    public long size() {
        return size(getReadPosition() & capacityMinusOne, writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne);
    }

    private long size(long readPosition, long writePosition) {
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return (writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne) == (getReadPosition() & capacityMinusOne);
    }

    private long getReadPosition() {
        readLock.lock();
        long readPosition = this.readPosition;
        readLock.unlock();
        return readPosition;
    }

    @Override
    public void writeByte(long offset, byte value) {
        putByte(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeChar(long offset, char value) {
        putChar(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeShort(long offset, short value) {
        putShort(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeInt(long offset, int value) {
        putInt(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeLong(long offset, long value) {
        putLong(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeBoolean(long offset, boolean value) {
        putBoolean(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeFloat(long offset, float value) {
        putFloat(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeDouble(long offset, double value) {
        putDouble(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public byte readByte(long offset) {
        return getByte(buffer, offset & capacityMinusOne);
    }

    @Override
    public char readChar(long offset) {
        return getChar(buffer, offset & capacityMinusOne);
    }

    @Override
    public short readShort(long offset) {
        return getShort(buffer, offset & capacityMinusOne);
    }

    @Override
    public int readInt(long offset) {
        return getInt(buffer, offset & capacityMinusOne);
    }

    @Override
    public long readLong(long offset) {
        return getLong(buffer, offset & capacityMinusOne);
    }

    @Override
    public boolean readBoolean(long offset) {
        return getBoolean(buffer, offset & capacityMinusOne);
    }

    @Override
    public float readFloat(long offset) {
        return getFloat(buffer, offset & capacityMinusOne);
    }

    @Override
    public double readDouble(long offset) {
        return getDouble(buffer, offset & capacityMinusOne);
    }
}
