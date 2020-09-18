/*
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
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

import static org.ringbuffer.marshalling.HeapBuffer.*;

@Contended
class FastAtomicReadHeapRingBuffer extends FastHeapRingBuffer {
    private static final long READ_POSITION = Unsafe.objectFieldOffset(FastAtomicReadHeapRingBuffer.class, "readPosition");

    private final int capacityMinusOne;
    private final byte[] buffer;
    private final boolean[] positionNotModified;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastAtomicReadHeapRingBuffer(HeapRingBufferBuilder builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        positionNotModified = builder.getPositionNotModified();
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
        AtomicBooleanArray.setRelease(positionNotModified, offset & capacityMinusOne, false);
    }

    @Override
    public int take(int size) {
        return take(size, HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    @Override
    public int take(int size, BusyWaitStrategy busyWaitStrategy) {
        int readPosition = AtomicInt.getAndAddVolatile(this, READ_POSITION, size) & capacityMinusOne;
        busyWaitStrategy.reset();
        while (AtomicBooleanArray.getAcquire(positionNotModified, readPosition)) {
            busyWaitStrategy.tick();
        }
        AtomicBooleanArray.setOpaque(positionNotModified, readPosition, true);
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
