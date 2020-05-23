package eu.menzani.ringbuffer.marshalling.array;

import eu.menzani.ringbuffer.java.Assume;

import static eu.menzani.ringbuffer.system.Unsafe.*;

public class UnsafeByteArray implements ByteArray {
    private static final long base = UNSAFE.arrayBaseOffset(byte[].class);
    private static final long scale = UNSAFE.arrayIndexScale(byte[].class);

    private static long offset(int index) {
        return base + scale * index;
    }

    private final byte[] array;

    public UnsafeByteArray(int length) {
        Assume.notGreater(length, Integer.MAX_VALUE - 8);
        array = new byte[length + 8];
    }

    @Override
    public void putByte(int index, byte value) {
        UNSAFE.putByte(array, offset(index), value);
    }

    @Override
    public void putChar(int index, char value) {
        UNSAFE.putChar(array, offset(index), value);
    }

    @Override
    public void putShort(int index, short value) {
        UNSAFE.putShort(array, offset(index), value);
    }

    @Override
    public void putInt(int index, int value) {
        UNSAFE.putInt(array, offset(index), value);
    }

    @Override
    public void putLong(int index, long value) {
        UNSAFE.putLong(array, offset(index), value);
    }

    @Override
    public void putBoolean(int index, boolean value) {
        UNSAFE.putBoolean(array, offset(index), value);
    }

    @Override
    public void putFloat(int index, float value) {
        UNSAFE.putFloat(array, offset(index), value);
    }

    @Override
    public void putDouble(int index, double value) {
        UNSAFE.putDouble(array, offset(index), value);
    }

    @Override
    public byte getByte(int index) {
        return UNSAFE.getByte(array, offset(index));
    }

    @Override
    public char getChar(int index) {
        return UNSAFE.getChar(array, offset(index));
    }

    @Override
    public short getShort(int index) {
        return UNSAFE.getShort(array, offset(index));
    }

    @Override
    public int getInt(int index) {
        return UNSAFE.getInt(array, offset(index));
    }

    @Override
    public long getLong(int index) {
        return UNSAFE.getLong(array, offset(index));
    }

    @Override
    public boolean getBoolean(int index) {
        return UNSAFE.getBoolean(array, offset(index));
    }

    @Override
    public float getFloat(int index) {
        return UNSAFE.getFloat(array, offset(index));
    }

    @Override
    public double getDouble(int index) {
        return UNSAFE.getDouble(array, offset(index));
    }
}
