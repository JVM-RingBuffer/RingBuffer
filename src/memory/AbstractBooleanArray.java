package eu.menzani.ringbuffer.memory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public abstract class AbstractBooleanArray implements BooleanArray {
    public static final VarHandle ELEMENTS = MethodHandles.arrayElementVarHandle(byte[].class);
    public static final byte TRUE = 1;
    public static final byte FALSE = 0;

    protected final byte[] elements;

    protected AbstractBooleanArray(int capacity) {
        elements = new byte[capacity];
    }
}
