package eu.menzani.ringbuffer.memory;

public class VolatileBooleanArray implements BooleanArray {
    private static final byte True = 1;
    private static final byte False = 0;

    private final byte[] elements;

    public VolatileBooleanArray(int capacity) {
        elements = new byte[capacity];
    }

    @Override
    public void setTrue(int index) {
        ARRAY.setVolatile(elements, index, True);
    }

    @Override
    public void setFalsePlain(int index) {
        elements[index] = False;
    }

    @Override
    public boolean isFalse(int index) {
        return (byte) ARRAY.getVolatile(elements, index) == False;
    }
}
