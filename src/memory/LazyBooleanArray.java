package eu.menzani.ringbuffer.memory;

public class LazyBooleanArray extends ByteBasedBooleanArray {
    public LazyBooleanArray(int capacity) {
        super(capacity);
    }

    @Override
    public void setTrue(int index) {
        ELEMENTS.setRelease(elements, index, TRUE);
    }

    @Override
    public void setFalse(int index) {
        ELEMENTS.setRelease(elements, index, FALSE);
    }

    @Override
    public void setFalsePlain(int index) {
        elements[index] = FALSE;
    }

    @Override
    public boolean isFalse(int index) {
        return (byte) ELEMENTS.getAcquire(elements, index) == FALSE;
    }

    @Override
    public boolean isTrue(int index) {
        return (byte) ELEMENTS.getAcquire(elements, index) == TRUE;
    }

    @Override
    public boolean weakCompareFalseAndSetTrue(int index) {
        return ELEMENTS.weakCompareAndSet(elements, index, FALSE, TRUE);
    }
}
