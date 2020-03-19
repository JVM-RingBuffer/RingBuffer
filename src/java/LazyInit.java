package eu.menzani.ringbuffer.java;

import java.lang.invoke.VarHandle;
import java.util.function.Function;

/**
 * Garbage-free, allocation-free lazy initialization helper
 */
public class LazyInit {
    public static <T, U> T get(VarHandle varHandle, U reference, Function<U, ?> initializer) {
        Object value = varHandle.getAcquire(reference);
        if (value == null) {
            value = checkAgain(varHandle, reference, initializer);
        }
        return (T) value;
    }

    private static <T> Object checkAgain(VarHandle varHandle, T reference, Function<T, ?> initializer) {
        synchronized (reference) {
            Object value = varHandle.getAcquire(reference);
            if (value == null) {
                value = initializer.apply(reference);
                varHandle.setRelease(reference, value);
            }
            return value;
        }
    }
}
