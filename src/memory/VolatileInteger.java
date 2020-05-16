package eu.menzani.ringbuffer.memory;

import eu.menzani.ringbuffer.java.AtomicInteger;

public class VolatileInteger implements Integer {
    private final AtomicInteger value = new AtomicInteger();

    @Override
    public void set(int value) {
        this.value.setVolatile(value);
    }

    @Override
    public int get() {
        return value.getVolatile();
    }

    @Override
    public int getPlain() {
        return value.getPlain();
    }
}
