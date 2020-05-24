package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.Lock;
import eu.menzani.ringbuffer.builder.NativeBlockingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.Proxy;
import eu.menzani.ringbuffer.marshalling.array.NativeByteArray;
import eu.menzani.ringbuffer.memory.Long;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.builder.Proxy.*;

class ConcurrentNativeBlockingRingBuffer implements NativeBlockingRingBuffer {
    private final long capacity;
    private final long capacityMinusOne;
    private final NativeByteArray buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final Lock writeLock = new Lock();
    private final Lock readLock = new Lock();

    private final Long readPosition;
    private final Long writePosition;

    ConcurrentNativeBlockingRingBuffer(NativeBlockingRingBufferBuilder builder) {
        capacity = Proxy.getCapacity(builder);
        capacityMinusOne = getCapacityMinusOne(builder);
        buffer = getBuffer(builder);
        readBusyWaitStrategy = getReadBusyWaitStrategy(builder);
        writeBusyWaitStrategy = getWriteBusyWaitStrategy(builder);
        readPosition = newCursor(builder);
        writePosition = newCursor(builder);
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long next(long size) {
        writeLock.lock();
        long writePosition = this.writePosition.getPlain() & capacityMinusOne;
        writeBusyWaitStrategy.reset();
        while (freeSpace(writePosition) < size) {
            writeBusyWaitStrategy.tick();
        }
        return this.writePosition.getPlain();
    }

    private long freeSpace(long writePosition) {
        long readPosition = this.readPosition.get() & capacityMinusOne;
        if (writePosition >= readPosition) {
            return capacity - (writePosition - readPosition);
        }
        return readPosition - writePosition;
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
    public void put(long offset) {
        writePosition.set(offset);
        writeLock.unlock();
    }

    @Override
    public long take(long size) {
        readLock.lock();
        long readPosition = this.readPosition.getPlain() & capacityMinusOne;
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
            readBusyWaitStrategy.tick();
        }
        return this.readPosition.getPlain();
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

    @Override
    public void advance(long offset) {
        readPosition.set(offset);
        readLock.unlock();
    }

    @Override
    public long size() {
        return size(readPosition.get() & capacityMinusOne);
    }

    private long size(long readPosition) {
        long writePosition = this.writePosition.get() & capacityMinusOne;
        if (writePosition >= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return (writePosition.get() & capacityMinusOne) == (readPosition.get() & capacityMinusOne);
    }
}
