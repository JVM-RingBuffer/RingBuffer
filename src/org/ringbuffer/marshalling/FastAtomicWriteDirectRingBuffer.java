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

import org.ringbuffer.concurrent.AtomicLong;
import org.ringbuffer.concurrent.DirectAtomicBooleanArray;
import org.ringbuffer.system.Unsafe;

import static org.ringbuffer.marshalling.DirectBuffer.*;

abstract class FastAtomicWriteDirectRingBuffer_pad0 extends FastDirectRingBuffer {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class FastAtomicWriteDirectRingBuffer_buf extends FastAtomicWriteDirectRingBuffer_pad0 {
    final long capacityMinusOne;
    final long buffer;
    final long writtenPositions;

    FastAtomicWriteDirectRingBuffer_buf(DirectRingBufferBuilder builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writtenPositions = builder.getWrittenPositions();
    }
}

abstract class FastAtomicWriteDirectRingBuffer_pad1 extends FastAtomicWriteDirectRingBuffer_buf {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastAtomicWriteDirectRingBuffer_pad1(DirectRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class FastAtomicWriteDirectRingBuffer_read extends FastAtomicWriteDirectRingBuffer_pad1 {
    long readPosition;

    FastAtomicWriteDirectRingBuffer_read(DirectRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class FastAtomicWriteDirectRingBuffer_pad2 extends FastAtomicWriteDirectRingBuffer_read {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastAtomicWriteDirectRingBuffer_pad2(DirectRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class FastAtomicWriteDirectRingBuffer_write extends FastAtomicWriteDirectRingBuffer_pad2 {
    long writePosition;

    FastAtomicWriteDirectRingBuffer_write(DirectRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class FastAtomicWriteDirectRingBuffer_pad3 extends FastAtomicWriteDirectRingBuffer_write {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastAtomicWriteDirectRingBuffer_pad3(DirectRingBufferBuilder builder) {
        super(builder);
    }
}

class FastAtomicWriteDirectRingBuffer extends FastAtomicWriteDirectRingBuffer_pad3 {
    private static final long WRITE_POSITION = Unsafe.objectFieldOffset(FastAtomicWriteDirectRingBuffer_write.class, "writePosition");

    FastAtomicWriteDirectRingBuffer(DirectRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public long getCapacity() {
        return capacityMinusOne + 1L;
    }

    @Override
    public long next(long size) {
        return AtomicLong.getAndAddVolatile(this, WRITE_POSITION, size);
    }

    @Override
    public void put(long offset) {
        DirectAtomicBooleanArray.setRelease(writtenPositions, offset & capacityMinusOne, false);
    }

    @Override
    public long take(long size) {
        long readPosition = this.readPosition & capacityMinusOne;
        this.readPosition += size;
        while (DirectAtomicBooleanArray.getAndSetVolatile(writtenPositions, readPosition, true)) {
            Thread.onSpinWait();
        }
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
