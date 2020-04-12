package eu.menzani.ringbuffer.memory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class LazyInteger implements Integer {
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(LazyInteger.class, "value", int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private int value;

    @Override
    public void set(int value) {
        VALUE.setRelease(this, value);
    }

    @Override
    public int getAndDecrement() {
        return (int) VALUE.getAndAdd(this, -1);
    }

    @Override
    public int get() {
        return (int) VALUE.getAcquire(this);
    }

    @Override
    public int getPlain() {
        return value;
    }
}
