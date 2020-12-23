package org.ringbuffer.marshalling;

import eu.menzani.atomic.AtomicLong;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.buffer.DirectBuffer.*;

@Contended
class AtomicReadDirectRingBuffer implements DirectClearingRingBuffer {
    private static final long WRITE_POSITION = Lang.objectFieldOffset(AtomicReadDirectRingBuffer.class, "writePosition");

    private final long capacity;
    private final long capacityMinusOne;
    private final long buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended("read")
    private long readPosition;
    @Contended
    private long writePosition;
    @Contended("read")
    private long cachedWritePosition;

    AtomicReadDirectRingBuffer(DirectClearingRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
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
        AtomicLong.setRelease(this, WRITE_POSITION, offset);
    }

    @Override
    public Object getReadMonitor() {
        return this;
    }

    @Override
    public long take(long size) {
        long readPosition = this.readPosition & capacityMinusOne;
        var readBusyWaitStrategy = this.readBusyWaitStrategy;
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
            cachedWritePosition = AtomicLong.getAcquire(this, WRITE_POSITION) & capacityMinusOne;
            return size(readPosition, cachedWritePosition) < size;
        }
        return false;
    }

    @Override
    public long size() {
        return size(getReadPosition() & capacityMinusOne, AtomicLong.getAcquire(this, WRITE_POSITION) & capacityMinusOne);
    }

    private long size(long readPosition, long writePosition) {
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return (AtomicLong.getAcquire(this, WRITE_POSITION) & capacityMinusOne) == (getReadPosition() & capacityMinusOne);
    }

    private synchronized long getReadPosition() {
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

    @Override
    public long take(long size, BusyWaitStrategy busyWaitStrategy) {
        throw new UnsupportedOperationException();
    }
}
