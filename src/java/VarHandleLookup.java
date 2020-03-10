package eu.menzani.ringbuffer.java;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class VarHandleLookup {
    private final MethodHandles.Lookup lookup;
    private final Class<?> clazz;

    public VarHandleLookup(MethodHandles.Lookup lookup, Class<?> clazz) {
        this.lookup = lookup.in(clazz);
        this.clazz = clazz;
    }

    public VarHandle getVarHandle(Class<?> fieldType, String fieldName) {
        try {
            return lookup.findVarHandle(clazz, fieldName, fieldType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
