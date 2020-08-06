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

class ConcurrentDirectRingBuffer implements DirectClearingRingBuffer {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(ConcurrentDirectRingBuffer.class, "writePosition");

    private final long capacity;
    private final long capacityMinusOne;
    private final long buffer;
    private final Lock readLock;
    private final Lock writeLock;
    private final BusyWaitStrategy readBusyWaitStrategy;

    private final LongHandle writePositionHandle;
    @Contended("read")
    private long readPosition;
    private long writePosition;
    @Contended("read")
    private long cachedWritePosition;

    ConcurrentDirectRingBuffer(DirectClearingRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readLock = builder.getReadLock();
        writeLock = builder.getWriteLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePositionHandle = builder.newHandle();
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long next() {
        writeLock.lock();
        return writePosition;
    }

    @Override
    public void put(long offset) {
        writePositionHandle.set(this, WRITE_POSITION, offset);
        writeLock.unlock();
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
        DirectBuffer.putByte(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeChar(long offset, char value) {
        DirectBuffer.putChar(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeShort(long offset, short value) {
        DirectBuffer.putShort(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeInt(long offset, int value) {
        DirectBuffer.putInt(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeLong(long offset, long value) {
        DirectBuffer.putLong(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeBoolean(long offset, boolean value) {
        DirectBuffer.putBoolean(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeFloat(long offset, float value) {
        DirectBuffer.putFloat(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeDouble(long offset, double value) {
        DirectBuffer.putDouble(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public byte readByte(long offset) {
        return DirectBuffer.getByte(buffer, offset & capacityMinusOne);
    }

    @Override
    public char readChar(long offset) {
        return DirectBuffer.getChar(buffer, offset & capacityMinusOne);
    }

    @Override
    public short readShort(long offset) {
        return DirectBuffer.getShort(buffer, offset & capacityMinusOne);
    }

    @Override
    public int readInt(long offset) {
        return DirectBuffer.getInt(buffer, offset & capacityMinusOne);
    }

    @Override
    public long readLong(long offset) {
        return DirectBuffer.getLong(buffer, offset & capacityMinusOne);
    }

    @Override
    public boolean readBoolean(long offset) {
        return DirectBuffer.getBoolean(buffer, offset & capacityMinusOne);
    }

    @Override
    public float readFloat(long offset) {
        return DirectBuffer.getFloat(buffer, offset & capacityMinusOne);
    }

    @Override
    public double readDouble(long offset) {
        return DirectBuffer.getDouble(buffer, offset & capacityMinusOne);
    }
}
