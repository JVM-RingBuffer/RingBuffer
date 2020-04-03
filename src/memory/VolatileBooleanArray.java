package eu.menzani.ringbuffer.memory;

public class VolatileBooleanArray extends AbstractBooleanArray {
    public VolatileBooleanArray(int capacity) {
        super(capacity);
    }

    @Override
    public void setTrue(int index) {
        ELEMENTS.setVolatile(elements, index, TRUE);
    }

    @Override
    public void setFalse(int index) {
        ELEMENTS.setVolatile(elements, index, FALSE);
    }

    @Override
    public void setFalsePlain(int index) {
        elements[index] = FALSE;
    }

    @Override
    public boolean isFalse(int index) {
        return (byte) ELEMENTS.getVolatile(elements, index) == FALSE;
    }

    @Override
    public boolean isTrue(int index) {
        return (byte) ELEMENTS.getVolatile(elements, index) == TRUE;
    }

    @Override
    public boolean weakCompareFalseAndSetTrue(int index) {
        return ELEMENTS.weakCompareAndSet(elements, index, FALSE, TRUE);
    }
}
