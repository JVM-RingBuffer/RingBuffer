package eu.menzani.ringbuffer.system;

import java.lang.reflect.Field;

public class Unsafe {
    public static final sun.misc.Unsafe UNSAFE;

    static {
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
