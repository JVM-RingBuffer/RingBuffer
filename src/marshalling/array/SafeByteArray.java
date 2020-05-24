package eu.menzani.ringbuffer.marshalling.array;

public class SafeByteArray extends UnsafeByteArray {
    private final int length;

    public SafeByteArray(int length) {
        super(length);
        this.length = length;
    }

    private void checkBounds(int index) {
        if (index < 0 || index >= length) {
            throw new ByteArrayIndexOutOfBoundsException(index);
        }
    }

    @Override
    public void putByte(int index, byte value) {
        checkBounds(index);
        super.putByte(index, value);
    }

    @Override
    public void putChar(int index, char value) {
        checkBounds(index);
        super.putChar(index, value);
    }

    @Override
    public void putShort(int index, short value) {
        checkBounds(index);
        super.putShort(index, value);
    }

    @Override
    public void putInt(int index, int value) {
        checkBounds(index);
        super.putInt(index, value);
    }

    @Override
    public void putLong(int index, long value) {
        checkBounds(index);
        super.putLong(index, value);
    }

    @Override
    public void putBoolean(int index, boolean value) {
        checkBounds(index);
        super.putBoolean(index, value);
    }

    @Override
    public void putFloat(int index, float value) {
        checkBounds(index);
        super.putFloat(index, value);
    }

    @Override
    public void putDouble(int index, double value) {
        checkBounds(index);
        super.putDouble(index, value);
    }

    @Override
    public byte getByte(int index) {
        checkBounds(index);
        return super.getByte(index);
    }

    @Override
    public char getChar(int index) {
        checkBounds(index);
        return super.getChar(index);
    }

    @Override
    public short getShort(int index) {
        checkBounds(index);
        return super.getShort(index);
    }

    @Override
    public int getInt(int index) {
        checkBounds(index);
        return super.getInt(index);
    }

    @Override
    public long getLong(int index) {
        checkBounds(index);
        return super.getLong(index);
    }

    @Override
    public boolean getBoolean(int index) {
        checkBounds(index);
        return super.getBoolean(index);
    }

    @Override
    public float getFloat(int index) {
        checkBounds(index);
        return super.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        checkBounds(index);
        return super.getDouble(index);
    }
}
