package org.ringbuffer.marshalling;

public interface DirectByteArray {
    void putByte(long index, byte value);

    void putChar(long index, char value);

    void putShort(long index, short value);

    void putInt(long index, int value);

    void putLong(long index, long value);

    void putBoolean(long index, boolean value);

    void putFloat(long index, float value);

    void putDouble(long index, double value);

    byte getByte(long index);

    char getChar(long index);

    short getShort(long index);

    int getInt(long index);

    long getLong(long index);

    boolean getBoolean(long index);

    float getFloat(long index);

    double getDouble(long index);

    interface Factory {
        DirectByteArray newInstance(long capacity);
    }
}