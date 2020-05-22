package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.AbstractRingBuffer;

interface AbstractHeapRingBuffer extends AbstractRingBuffer {
    void writeByte(int offset, byte value);

    void writeChar(int offset, char value);

    void writeShort(int offset, short value);

    void writeInt(int offset, int value);

    void writeLong(int offset, long value);

    void writeBoolean(int offset, boolean value);

    void writeFloat(int offset, float value);

    void writeDouble(int offset, double value);

    void put(int offset);

    int take(int size);

    byte readByte(int offset);

    char readChar(int offset);

    short readShort(int offset);

    int readInt(int offset);

    long readLong(int offset);

    boolean readBoolean(int offset);

    float readFloat(int offset);

    double readDouble(int offset);

    void advance(int offset);

    int size();
}
