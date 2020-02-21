package eu.menzani.ringbuffer;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class LazyVolatileInteger {
    private static final VarHandle handle;

    static {
        try {
            final Class<?> clazz = LazyVolatileInteger.class;
            handle = MethodHandles.lookup()
                    .in(clazz)
                    .findVarHandle(clazz, "value", int.class);
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
