package eu.menzani.ringbuffer;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

class LazyVolatileBooleanArray {
    private static final VarHandle ARRAY = MethodHandles.arrayElementVarHandle(byte[].class);
    private static final byte True = 1;
    private static final byte False = 0;

    private final byte[] elements;

    LazyVolatileBooleanArray(int capacity) {
        elements = new byte[capacity];
    }

    void setTrue(int index) {
        ARRAY.setRelease(elements, index, True);
    }

    void setFalsePlain(int index) {
        elements[index] = False;
    }

    boolean isFalse(int index) {
        return (byte) ARRAY.getAcquire(elements, index) == False;
    }
}
