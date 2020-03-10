package eu.menzani.ringbuffer.java;

import java.lang.invoke.VarHandle;
import java.util.function.Supplier;

public class LazyInit {
    public static <T> T get(VarHandle varHandle, Object object, Supplier<? extends T> initializer) {
        T value = (T) varHandle.getAcquire(object);
        if (value == null) {
            synchronized (object) {
                value = (T) varHandle.getAcquire(object);
                if (value == null) {
                    value = initializer.get();
                    varHandle.setRelease(object, value);
                }
            }
        }
        return value;
    }
}
