package eu.menzani.ringbuffer;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

class LazyVolatileInteger {
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(LazyVolatileInteger.class, "value", int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private int value;

    void set(int value) {
        VALUE.setRelease(this, value);
    }

    int get() {
        return (int) VALUE.getAcquire(this);
    }

    int getPlain() {
        return value;
    }
}
