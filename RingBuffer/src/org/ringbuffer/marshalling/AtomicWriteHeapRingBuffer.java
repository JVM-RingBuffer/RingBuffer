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
import org.ringbuffer.concurrent.AtomicInt;
import org.ringbuffer.lang.Lang;
import org.ringbuffer.wait.BusyWaitStrategy;

import static org.ringbuffer.marshalling.HeapBuffer.*;

@Contended
class AtomicWriteHeapRingBuffer implements HeapClearingRingBuffer {
    private static final long WRITE_POSITION = Lang.objectFieldOffset(AtomicWriteHeapRingBuffer.class, "writePosition");

    private final int capacity;
    private final int capacityMinusOne;
    private final byte[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended("read")
    private int readPosition;
    @Contended
    private int writePosition;
    @Contended("read")
    private int cachedWritePosition;

    AtomicWriteHeapRingBuffer(HeapClearingRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int next() {
        return writePosition;
    }

    @Override
    public void put(int offset) {
        AtomicInt.setRelease(this, WRITE_POSITION, offset);
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
            cachedWritePosition = AtomicInt.getAcquire(this, WRITE_POSITION) & capacityMinusOne;
            return size(readPosition, cachedWritePosition) < size;
        }
        return false;
    }

    @Override
    public int size() {
        return size(readPosition & capacityMinusOne, AtomicInt.getAcquire(this, WRITE_POSITION) & capacityMinusOne);
    }

    private int size(int readPosition, int writePosition) {
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return (AtomicInt.getAcquire(this, WRITE_POSITION) & capacityMinusOne) == (readPosition & capacityMinusOne);
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

    @Override
    public Object getReadMonitor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int take(int size, BusyWaitStrategy busyWaitStrategy) {
        throw new UnsupportedOperationException();
    }
}
