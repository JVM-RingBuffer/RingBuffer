package eu.menzani.ringbuffer.java;

import java.lang.invoke.VarHandle;

/**
 * Garbage-free, allocation-free lazy initialization helper
 */
public class LazyInit {
    public static <T> T get(VarHandle varHandle, Object reference) {
        Object value = varHandle.getAcquire(reference);
        if (value == null) {
            synchronized (reference) {
                value = varHandle.getAcquire(reference);
            }
        }
        return (T) value;
    }

    public static <T> T initialize(VarHandle varHandle, Object reference, T value) {
        varHandle.setRelease(reference, value);
        return value;
    }
}
