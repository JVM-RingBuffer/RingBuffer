package eu.menzani.ringbuffer;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class LazyVolatileInteger {
    private static final VarHandle handle;

    static {
        try {
            handle = MethodHandles.lookup().in(LazyVolatileInteger.class).findVarHandle(LazyVolatileInteger.class, "value", int.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

    private int value;

    public void set(int value) {
        handle.setRelease(this, value);
    }

    public int get() {
        return (int) handle.getAcquire(this);
    }

    public int getFromSameThread() {
        return value;
    }
}
