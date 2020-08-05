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

import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.lock.Lock;
import org.ringbuffer.memory.LongHandle;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;

class AtomicWriteDirectBlockingRingBuffer implements DirectRingBuffer {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        final Class<?> clazz = AtomicWriteDirectBlockingRingBuffer.class;
        READ_POSITION = Unsafe.objectFieldOffset(clazz, "readPosition");
        WRITE_POSITION = Unsafe.objectFieldOffset(clazz, "writePosition");
    }

    private final long capacity;
    private final long capacityMinusOne;
    private final DirectByteArray buffer;
    private final Lock writeLock;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final LongHandle readPositionHandle;
    private final LongHandle writePositionHandle;
    @Contended("read")
    private long readPosition;
    private long writePosition;
    @Contended("read")
    private long cachedWritePosition;

    AtomicWriteDirectBlockingRingBuffer(DirectRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writeLock = builder.getWriteLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
        readPositionHandle = builder.newHandle();
        writePositionHandle = builder.newHandle();
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long next(long size) {
        writeLock.lock();
        long writePosition = this.writePosition & capacityMinusOne;
        writeBusyWaitStrategy.reset();
        while (freeSpace(writePosition) <= size) {
            writeBusyWaitStrategy.tick();
        }
        return writePosition;
    }

    private long freeSpace(long writePosition) {
        long readPosition = readPositionHandle.get(this, READ_POSITION) & capacityMinusOne;
        if (writePosition >= readPosition) {
            return capacity - (writePosition - readPosition);
        }
        return readPosition - writePosition;
    }

    @Override
    public void put(long offset) {
        writePositionHandle.set(this, WRITE_POSITION, offset);
        writeLock.unlock();
    }

    @Override
    public long take(long size) {
        long readPosition = this.readPosition & capacityMinusOne;
        readBusyWaitStrategy.reset();
        while (isNotFullEnoughCached(readPosition, size)) {
            readBusyWaitStrategy.tick();
        }
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
    public void advance(long offset) {
        readPositionHandle.set(this, READ_POSITION, offset);
    }

    @Override
    public long size() {
        return size(readPositionHandle.get(this, READ_POSITION) & capacityMinusOne, writePositionHandle.get(this, WRITE_POSITION) & capacityMinusOne);
    }

    private long size(long readPosition, long writePosition) {
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
    public void writeByte(long offset, byte value) {
        buffer.putByte(offset & capacityMinusOne, value);
    }

    @Override
    public void writeChar(long offset, char value) {
        buffer.putChar(offset & capacityMinusOne, value);
    }

    @Override
    public void writeShort(long offset, short value) {
        buffer.putShort(offset & capacityMinusOne, value);
    }

    @Override
    public void writeInt(long offset, int value) {
        buffer.putInt(offset & capacityMinusOne, value);
    }

    @Override
    public void writeLong(long offset, long value) {
        buffer.putLong(offset & capacityMinusOne, value);
    }

    @Override
    public void writeBoolean(long offset, boolean value) {
        buffer.putBoolean(offset & capacityMinusOne, value);
    }

    @Override
    public void writeFloat(long offset, float value) {
        buffer.putFloat(offset & capacityMinusOne, value);
    }

    @Override
    public void writeDouble(long offset, double value) {
        buffer.putDouble(offset & capacityMinusOne, value);
    }

    @Override
    public byte readByte(long offset) {
        return buffer.getByte(offset & capacityMinusOne);
    }

    @Override
    public char readChar(long offset) {
        return buffer.getChar(offset & capacityMinusOne);
    }

    @Override
    public short readShort(long offset) {
        return buffer.getShort(offset & capacityMinusOne);
    }

    @Override
    public int readInt(long offset) {
        return buffer.getInt(offset & capacityMinusOne);
    }

    @Override
    public long readLong(long offset) {
        return buffer.getLong(offset & capacityMinusOne);
    }

    @Override
    public boolean readBoolean(long offset) {
        return buffer.getBoolean(offset & capacityMinusOne);
    }

    @Override
    public float readFloat(long offset) {
        return buffer.getFloat(offset & capacityMinusOne);
    }

    @Override
    public double readDouble(long offset) {
        return buffer.getDouble(offset & capacityMinusOne);
    }
}
