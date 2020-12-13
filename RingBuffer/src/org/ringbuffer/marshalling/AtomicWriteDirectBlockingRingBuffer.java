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
import org.ringbuffer.concurrent.AtomicLong;
import org.ringbuffer.lang.Lang;
import org.ringbuffer.wait.BusyWaitStrategy;

import static org.ringbuffer.marshalling.DirectBuffer.*;

@Contended
class AtomicWriteDirectBlockingRingBuffer implements DirectRingBuffer {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        final Class<?> clazz = AtomicWriteDirectBlockingRingBuffer.class;
        READ_POSITION = Lang.objectFieldOffset(clazz, "readPosition");
        WRITE_POSITION = Lang.objectFieldOffset(clazz, "writePosition");
    }

    private final long capacity;
    private final long capacityMinusOne;
    private final long buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    @Contended("read")
    private long readPosition;
    @Contended("write")
    private long writePosition;
    @Contended("write")
    private long cachedReadPosition;
    @Contended("read")
    private long cachedWritePosition;

    AtomicWriteDirectBlockingRingBuffer(DirectRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long next(long size) {
        long writePosition = this.writePosition & capacityMinusOne;
        var writeBusyWaitStrategy = this.writeBusyWaitStrategy;
        writeBusyWaitStrategy.reset();
        while (isThereNotEnoughFreeSpaceCached(writePosition, size)) {
            writeBusyWaitStrategy.tick();
        }
        return writePosition;
    }

    private boolean isThereNotEnoughFreeSpaceCached(long writePosition, long size) {
        if (freeSpace(writePosition, cachedReadPosition) <= size) {
            cachedReadPosition = AtomicLong.getAcquire(this, READ_POSITION) & capacityMinusOne;
            return freeSpace(writePosition, cachedReadPosition) <= size;
        }
        return false;
    }

    private long freeSpace(long writePosition, long readPosition) {
        if (writePosition >= readPosition) {
            return capacity - (writePosition - readPosition);
        }
        return readPosition - writePosition;
    }

    @Override
    public void put(long offset) {
        AtomicLong.setRelease(this, WRITE_POSITION, offset);
    }

    @Override
    public long take(long size) {
        long readPosition = this.readPosition & capacityMinusOne;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
        readBusyWaitStrategy.reset();
        while (isNotFullEnoughCached(readPosition, size)) {
            readBusyWaitStrategy.tick();
        }
        return readPosition;
    }

    private boolean isNotFullEnoughCached(long readPosition, long size) {
        if (size(readPosition, cachedWritePosition) < size) {
            cachedWritePosition = AtomicLong.getAcquire(this, WRITE_POSITION) & capacityMinusOne;
            return size(readPosition, cachedWritePosition) < size;
        }
        return false;
    }

    @Override
    public void advance(long offset) {
        AtomicLong.setRelease(this, READ_POSITION, offset);
    }

    @Override
    public long size() {
        return size(AtomicLong.getAcquire(this, READ_POSITION) & capacityMinusOne, AtomicLong.getAcquire(this, WRITE_POSITION) & capacityMinusOne);
    }

    private long size(long readPosition, long writePosition) {
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return (AtomicLong.getAcquire(this, WRITE_POSITION) & capacityMinusOne) == (AtomicLong.getAcquire(this, READ_POSITION) & capacityMinusOne);
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

    @Override
    public Object getReadMonitor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long take(long size, BusyWaitStrategy busyWaitStrategy) {
        throw new UnsupportedOperationException();
    }
}
