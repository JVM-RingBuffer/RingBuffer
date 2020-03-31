package eu.menzani.ringbuffer.memory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public interface BooleanArray {
    VarHandle ARRAY = MethodHandles.arrayElementVarHandle(byte[].class);

    void setTrue(int index);

    void setFalsePlain(int index);

    boolean isFalse(int index);
}
