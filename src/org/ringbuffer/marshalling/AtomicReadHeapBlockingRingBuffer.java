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
import org.ringbuffer.memory.IntHandle;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;

class AtomicReadHeapBlockingRingBuffer implements HeapRingBuffer {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        final Class<?> clazz = AtomicReadHeapBlockingRingBuffer.class;
        READ_POSITION = Unsafe.objectFieldOffset(clazz, "readPosition");
        WRITE_POSITION = Unsafe.objectFieldOffset(clazz, "writePosition");
    }

    private final int capacity;
    private final int capacityMinusOne;
    private final byte[] buffer;
    private final Lock readLock;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final IntHandle readPositionHandle;
    private final IntHandle writePositionHandle;
    @Contended("read")
    private int readPosition;
    private int writePosition;
    @Contended("read")
    private int cachedWritePosition;

    AtomicReadHeapBlockingRingBuffer(HeapRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readLock = builder.getReadLock();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
        readPositionHandle = builder.newHandle();
        writePositionHandle = builder.newHandle();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int next(int size) {
        int writePosition = this.writePosition & capacityMinusOne;
        writeBusyWaitStrategy.reset();
        while (freeSpace(writePosition) <= size) {
            writeBusyWaitStrategy.tick();
        }
        return writePosition;
    }

    private int freeSpace(int writePosition) {
        int readPosition = readPositionHandle.get(this, READ_POSITION) & capacityMinusOne;
        if (writePosition >= readPosition) {
            return capacity - (writePosition - readPosition);
        }
        return readPosition - writePosition;
    }

    @Override
    public void put(int offset) {
        writePositionHandle.set(this, WRITE_POSITION, offset);
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
        HeapBuffer.putByte(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeChar(int offset, char value) {
        HeapBuffer.putChar(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeShort(int offset, short value) {
        HeapBuffer.putShort(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeInt(int offset, int value) {
        HeapBuffer.putInt(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeLong(int offset, long value) {
        HeapBuffer.putLong(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeBoolean(int offset, boolean value) {
        HeapBuffer.putBoolean(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeFloat(int offset, float value) {
        HeapBuffer.putFloat(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public void writeDouble(int offset, double value) {
        HeapBuffer.putDouble(buffer, offset & capacityMinusOne, value);
    }

    @Override
    public byte readByte(int offset) {
        return HeapBuffer.getByte(buffer, offset & capacityMinusOne);
    }

    @Override
    public char readChar(int offset) {
        return HeapBuffer.getChar(buffer, offset & capacityMinusOne);
    }

    @Override
    public short readShort(int offset) {
        return HeapBuffer.getShort(buffer, offset & capacityMinusOne);
    }

    @Override
    public int readInt(int offset) {
        return HeapBuffer.getInt(buffer, offset & capacityMinusOne);
    }

    @Override
    public long readLong(int offset) {
        return HeapBuffer.getLong(buffer, offset & capacityMinusOne);
    }

    @Override
    public boolean readBoolean(int offset) {
        return HeapBuffer.getBoolean(buffer, offset & capacityMinusOne);
    }

    @Override
    public float readFloat(int offset) {
        return HeapBuffer.getFloat(buffer, offset & capacityMinusOne);
    }

    @Override
    public double readDouble(int offset) {
        return HeapBuffer.getDouble(buffer, offset & capacityMinusOne);
    }
}
