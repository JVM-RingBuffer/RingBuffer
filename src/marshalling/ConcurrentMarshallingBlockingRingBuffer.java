package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.Lock;
import eu.menzani.ringbuffer.builder.MarshallingBlockingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.Proxy;
import eu.menzani.ringbuffer.marshalling.array.ByteArray;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.builder.Proxy.*;

class ConcurrentMarshallingBlockingRingBuffer implements MarshallingBlockingRingBuffer {
    private final int capacity;
    private final int capacityMinusOne;
    private final ByteArray buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final Lock writeLock = new Lock();
    private final Lock readLock = new Lock();

    private final Integer readPosition;
    private final Integer writePosition;

    ConcurrentMarshallingBlockingRingBuffer(MarshallingBlockingRingBufferBuilder builder) {
        capacity = Proxy.getCapacity(builder);
        capacityMinusOne = getCapacityMinusOne(builder);
        buffer = getBuffer(builder);
        readBusyWaitStrategy = getReadBusyWaitStrategy(builder);
        writeBusyWaitStrategy = getWriteBusyWaitStrategy(builder);
        readPosition = newCursor(builder);
        writePosition = newCursor(builder);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int next(int size) {
        writeLock.lock();
        int writePosition = this.writePosition.getPlain() & capacityMinusOne;
        writeBusyWaitStrategy.reset();
        while (freeSpace(writePosition) < size) {
            writeBusyWaitStrategy.tick();
        }
        return this.writePosition.getPlain();
    }

    private int freeSpace(int writePosition) {
        int readPosition = this.readPosition.get() & capacityMinusOne;
        if (writePosition >= readPosition) {
            return capacity - (writePosition - readPosition);
        }
        return readPosition - writePosition;
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
    public void put(int offset) {
        writePosition.set(offset);
        writeLock.unlock();
    }

    @Override
    public int take(int size) {
        readLock.lock();
        int readPosition = this.readPosition.getPlain() & capacityMinusOne;
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
            readBusyWaitStrategy.tick();
        }
        return this.readPosition.getPlain();
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

    @Override
    public void advance(int offset) {
        readPosition.set(offset);
        readLock.unlock();
    }

    @Override
    public int size() {
        return size(readPosition.get() & capacityMinusOne);
    }

    private int size(int readPosition) {
        int writePosition = this.writePosition.get() & capacityMinusOne;
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