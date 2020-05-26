package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

class VolatileMarshallingBlockingRingBuffer implements MarshallingBlockingRingBuffer {
    private final int capacity;
    private final int capacityMinusOne;
    private final ByteArray buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final Integer readPosition;
    private final Integer writePosition;

    VolatileMarshallingBlockingRingBuffer(MarshallingBlockingRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
        readPosition = builder.newCursor();
        writePosition = builder.newCursor();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int next(int size) {
        int writePosition = this.writePosition.getPlain() & capacityMinusOne;
        writeBusyWaitStrategy.reset();
        while (freeSpace(writePosition) <= size) {
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
    public void put(int offset) {
        writePosition.set(offset);
    }

    @Override
    public int take(int size) {
        int readPosition = this.readPosition.getPlain() & capacityMinusOne;
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
            readBusyWaitStrategy.tick();
        }
        return this.readPosition.getPlain();
    }

    @Override
    public void advance(int offset) {
        readPosition.set(offset);
    }

    @Override
    public int size() {
        return size(readPosition.get() & capacityMinusOne);
    }

    private int size(int readPosition) {
        int writePosition = this.writePosition.get() & capacityMinusOne;
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return (writePosition.get() & capacityMinusOne) == (readPosition.get() & capacityMinusOne);
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
