package eu.menzani.ringbuffer.memory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public abstract class AbstractBooleanArray implements BooleanArray {
    protected static final VarHandle ELEMENTS = MethodHandles.arrayElementVarHandle(byte[].class);
    protected static final byte TRUE = 1;
    protected static final byte FALSE = 0;

    protected final byte[] elements;

    protected AbstractBooleanArray(int capacity) {
        elements = new byte[capacity];
    }
}
