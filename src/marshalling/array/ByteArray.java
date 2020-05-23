package eu.menzani.ringbuffer.marshalling.array;

public interface ByteArray {
    void putByte(int index, byte value);

    void putChar(int index, char value);

    void putShort(int index, short value);

    void putInt(int index, int value);

    void putLong(int index, long value);

    void putBoolean(int index, boolean value);

    void putFloat(int index, float value);

    void putDouble(int index, double value);

    byte getByte(int index);

    char getChar(int index);

    short getShort(int index);

    int getInt(int index);

    long getLong(int index);

    boolean getBoolean(int index);

    float getFloat(int index);

    double getDouble(int index);
}
