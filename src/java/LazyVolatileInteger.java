package eu.menzani.ringbuffer.java;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class LazyVolatileInteger {
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(LazyVolatileInteger.class, "value", int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private int value;

    public LazyVolatileInteger(int value) {
        this.value = value;
    }

    public int decrementAndGetPlain() {
        return (int) VALUE.getAndAddRelease(this, -1) - 1;
    }

    public int getPlainAndIncrement() {
        return (int) VALUE.getAndAddRelease(this, 1) + 1;
    }

    public int get() {
        return (int) VALUE.getAcquire(this);
    }

    public int getPlain() {
        return value;
    }
}
