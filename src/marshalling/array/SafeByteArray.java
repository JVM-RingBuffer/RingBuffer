package eu.menzani.ringbuffer.marshalling.array;

import eu.menzani.ringbuffer.java.Assume;

public class SafeByteArray implements ByteArray {
    private final byte[] array;

    public SafeByteArray(int length) {
        Assume.notGreater(length, Integer.MAX_VALUE - 8);
        array = new byte[length + 8];
    }

    @Override
    public void putByte(int index, byte value) {
        array[index] = value;
    }

    @Override
    public void putChar(int index, char value) {
        array[index] = (byte) ((value >>> 8) & 0xFF);
        array[index + 1] = (byte) (value & 0xFF);
    }

    @Override
    public void putShort(int index, short value) {
        array[index] = (byte) ((value >>> 8) & 0xFF);
        array[index + 1] = (byte) (value & 0xFF);
    }

    @Override
    public void putInt(int index, int value) {
        array[index] = (byte) ((value >>> 24) & 0xFF);
        array[index + 1] = (byte) ((value >>> 16) & 0xFF);
        array[index + 2] = (byte) ((value >>> 8) & 0xFF);
        array[index + 3] = (byte) (value & 0xFF);
    }

    @Override
    public void putLong(int index, long value) {
        array[index] = (byte) (value >>> 56);
        array[index + 1] = (byte) (value >>> 48);
        array[index + 2] = (byte) (value >>> 40);
        array[index + 3] = (byte) (value >>> 32);
        array[index + 4] = (byte) (value >>> 24);
        array[index + 5] = (byte) (value >>> 16);
        array[index + 6] = (byte) (value >>> 8);
        array[index + 7] = (byte) value;
    }

    @Override
    public void putBoolean(int index, boolean value) {
        putByte(index, value ? (byte) 1 : (byte) 0);
    }

    @Override
    public void putFloat(int index, float value) {
        putInt(index, Float.floatToIntBits(value));
    }

    @Override
    public void putDouble(int index, double value) {
        putLong(index, Double.doubleToLongBits(value));
    }

    @Override
    public byte getByte(int index) {
        return array[index];
    }

    @Override
    public char getChar(int index) {
        return (char) ((array[index] << 8) + array[index + 1]);
    }

    @Override
    public short getShort(int index) {
        return (short) ((array[index] << 8) + array[index + 1]);
    }

    @Override
    public int getInt(int index) {
        return (array[index] << 24) +
                (array[index + 1] << 16) +
                (array[index + 2] << 8) +
                array[index + 3];
    }

    @Override
    public long getLong(int index) {
        return ((long) array[index] << 56) +
                ((long) (array[index + 1] & 255) << 48) +
                ((long) (array[index + 2] & 255) << 40) +
                ((long) (array[index + 3] & 255) << 32) +
                ((long) (array[index + 4] & 255) << 24) +
                ((array[index + 5] & 255) << 16) +
                ((array[index + 6] & 255) << 8) +
                (array[index + 7] & 255);
    }

    @Override
    public boolean getBoolean(int index) {
        return getByte(index) == (byte) 1;
    }

    @Override
    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }

    @Override
    public double getDouble(int index) {
        return Double.longBitsToDouble(getLong(index));
    }
}
