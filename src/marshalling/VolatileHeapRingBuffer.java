package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.builder.HeapRingBufferBuilder;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.marshalling.Marshaller.*;

class VolatileHeapRingBuffer implements HeapRingBuffer {
    private final int capacity;
    private final int capacityMinusOne;
    private final byte[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    private int readPosition;
    private final Integer writePosition;

    VolatileHeapRingBuffer(HeapRingBufferBuilder builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePosition = builder.newCursor();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int next() {
        return writePosition.getPlain();
    }

    @Override
    public void writeByte(int offset, byte value) {
        serializeByte(buffer, capacityMinusOne, offset, value);
    }

    @Override
    public void writeChar(int offset, char value) {
        serializeChar(buffer, capacityMinusOne, offset, value);
    }

    @Override
    public void writeShort(int offset, short value) {
        serializeShort(buffer, capacityMinusOne, offset, value);
    }

    @Override
    public void writeInt(int offset, int value) {
        serializeInt(buffer, capacityMinusOne, offset, value);
    }

    @Override
    public void writeLong(int offset, long value) {
        serializeLong(buffer, capacityMinusOne, offset, value);
    }

    @Override
    public void writeBoolean(int offset, boolean value) {
        serializeBoolean(buffer, capacityMinusOne, offset, value);
    }

    @Override
    public void writeFloat(int offset, float value) {
        serializeFloat(buffer, capacityMinusOne, offset, value);
    }

    @Override
    public void writeDouble(int offset, double value) {
        serializeDouble(buffer, capacityMinusOne, offset, value);
    }

    @Override
    public void put(int offset) {
        writePosition.set(offset);
    }

    @Override
    public int take(int size) {
        int readPosition = this.readPosition & capacityMinusOne;
        readBusyWaitStrategy.reset();
        while (size(readPosition) < size) {
            readBusyWaitStrategy.tick();
        }
        return this.readPosition;
    }

    @Override
    public byte readByte(int offset) {
        return deserializeByte(buffer, capacityMinusOne, offset);
    }

    @Override
    public char readChar(int offset) {
        return deserializeChar(buffer, capacityMinusOne, offset);
    }

    @Override
    public short readShort(int offset) {
        return deserializeShort(buffer, capacityMinusOne, offset);
    }

    @Override
    public int readInt(int offset) {
        return deserializeInt(buffer, capacityMinusOne, offset);
    }

    @Override
    public long readLong(int offset) {
        return deserializeLong(buffer, capacityMinusOne, offset);
    }

    @Override
    public boolean readBoolean(int offset) {
        return deserializeBoolean(buffer, capacityMinusOne, offset);
    }

    @Override
    public float readFloat(int offset) {
        return deserializeFloat(buffer, capacityMinusOne, offset);
    }

    @Override
    public double readDouble(int offset) {
        return deserializeDouble(buffer, capacityMinusOne, offset);
    }

    @Override
    public void advance(int offset) {
        readPosition = offset;
    }

    @Override
    public int size() {
        return size(readPosition & capacityMinusOne);
    }

    private int size(int readPosition) {
        int writePosition = this.writePosition.get() & capacityMinusOne;
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return (writePosition.get() & capacityMinusOne) == (readPosition & capacityMinusOne);
    }
}
