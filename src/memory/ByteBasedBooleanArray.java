package eu.menzani.ringbuffer.memory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public abstract class ByteBasedBooleanArray implements BooleanArray {
    public static final VarHandle ELEMENTS = MethodHandles.arrayElementVarHandle(byte[].class);
    public static final byte TRUE = 1;
    public static final byte FALSE = 0;

    protected final byte[] elements;

    protected ByteBasedBooleanArray(int capacity) {
        elements = new byte[capacity];
    }
}
