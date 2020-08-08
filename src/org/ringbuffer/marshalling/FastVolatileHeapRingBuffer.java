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

import org.ringbuffer.concurrent.AtomicBooleanArray;

import static org.ringbuffer.marshalling.HeapBuffer.*;

abstract class FastVolatileHeapRingBuffer_pad0 extends FastHeapRingBuffer {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class FastVolatileHeapRingBuffer_buf extends FastVolatileHeapRingBuffer_pad0 {
    final int capacityMinusOne;
    final byte[] buffer;
    final boolean[] writtenPositions;

    FastVolatileHeapRingBuffer_buf(HeapRingBufferBuilder builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        writtenPositions = builder.getWrittenPositions();
    }
}

abstract class FastVolatileHeapRingBuffer_pad1 extends FastVolatileHeapRingBuffer_buf {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatileHeapRingBuffer_pad1(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class FastVolatileHeapRingBuffer_read extends FastVolatileHeapRingBuffer_pad1 {
    int readPosition;

    FastVolatileHeapRingBuffer_read(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class FastVolatileHeapRingBuffer_pad2 extends FastVolatileHeapRingBuffer_read {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatileHeapRingBuffer_pad2(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class FastVolatileHeapRingBuffer_write extends FastVolatileHeapRingBuffer_pad2 {
    int writePosition;

    FastVolatileHeapRingBuffer_write(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

abstract class FastVolatileHeapRingBuffer_pad3 extends FastVolatileHeapRingBuffer_write {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;

    FastVolatileHeapRingBuffer_pad3(HeapRingBufferBuilder builder) {
        super(builder);
    }
}

class FastVolatileHeapRingBuffer extends FastVolatileHeapRingBuffer_pad3 {
    FastVolatileHeapRingBuffer(HeapRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public int getCapacity() {
        return capacityMinusOne + 1;
    }

    @Override
    public int next(int size) {
        int writePosition = this.writePosition;
        this.writePosition += size;
        return writePosition;
    }

    @Override
    public void put(int offset) {
        AtomicBooleanArray.setRelease(writtenPositions, offset & capacityMinusOne, false);
    }

    @Override
    public int take(int size) {
        int readPosition = this.readPosition & capacityMinusOne;
        this.readPosition += size;
        while (AtomicBooleanArray.getAcquire(writtenPositions, readPosition)) {
            Thread.onSpinWait();
        }
        AtomicBooleanArray.setPlain(writtenPositions, readPosition, true);
        return readPosition;
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
