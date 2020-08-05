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
import org.ringbuffer.concurrent.AtomicBooleanArray;
import org.ringbuffer.concurrent.AtomicInt;
import org.ringbuffer.system.Unsafe;

class FastAtomicWriteHeapRingBuffer extends FastHeapRingBuffer {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(FastAtomicWriteHeapRingBuffer.class, "writePosition");

    private final int capacityMinusOne;
    private final ByteArray buffer;
    private final boolean[] writtenPositions;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastAtomicWriteHeapRingBuffer(HeapRingBufferBuilder builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writtenPositions = builder.getWrittenPositions();
    }

    @Override
    public int getCapacity() {
        return capacityMinusOne + 1;
    }

    @Override
    public int next(int size) {
        return AtomicInt.getAndAddVolatile(this, WRITE_POSITION, size);
    }

    @Override
    public void put(int offset) {
        AtomicBooleanArray.setRelease(writtenPositions, offset & capacityMinusOne, false);
    }

    @Override
    public int take(int size) {
        int readPosition = this.readPosition & capacityMinusOne;
        this.readPosition += size;
        while (AtomicBooleanArray.getAndSetVolatile(writtenPositions, readPosition, true)) {
            Thread.onSpinWait();
        }
        return readPosition;
    }

    @Override
    public void writeByte(int offset, byte value) {
        buffer.putByte(offset & capacityMinusOne, value);
    }

    @Override
    public void writeChar(int offset, char value) {
        buffer.putChar(offset & capacityMinusOne, value);
    }

    @Override
    public void writeShort(int offset, short value) {
        buffer.putShort(offset & capacityMinusOne, value);
    }

    @Override
    public void writeInt(int offset, int value) {
        buffer.putInt(offset & capacityMinusOne, value);
    }

    @Override
    public void writeLong(int offset, long value) {
        buffer.putLong(offset & capacityMinusOne, value);
    }

    @Override
    public void writeBoolean(int offset, boolean value) {
        buffer.putBoolean(offset & capacityMinusOne, value);
    }

    @Override
    public void writeFloat(int offset, float value) {
        buffer.putFloat(offset & capacityMinusOne, value);
    }

    @Override
    public void writeDouble(int offset, double value) {
        buffer.putDouble(offset & capacityMinusOne, value);
    }

    @Override
    public byte readByte(int offset) {
        return buffer.getByte(offset & capacityMinusOne);
    }

    @Override
    public char readChar(int offset) {
        return buffer.getChar(offset & capacityMinusOne);
    }

    @Override
    public short readShort(int offset) {
        return buffer.getShort(offset & capacityMinusOne);
    }

    @Override
    public int readInt(int offset) {
        return buffer.getInt(offset & capacityMinusOne);
    }

    @Override
    public long readLong(int offset) {
        return buffer.getLong(offset & capacityMinusOne);
    }

    @Override
    public boolean readBoolean(int offset) {
        return buffer.getBoolean(offset & capacityMinusOne);
    }

    @Override
    public float readFloat(int offset) {
        return buffer.getFloat(offset & capacityMinusOne);
    }

    @Override
    public double readDouble(int offset) {
        return buffer.getDouble(offset & capacityMinusOne);
    }
}
