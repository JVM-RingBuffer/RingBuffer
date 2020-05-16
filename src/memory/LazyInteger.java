package eu.menzani.ringbuffer.memory;

import eu.menzani.ringbuffer.java.AtomicInteger;

public class LazyInteger implements Integer {
    private final AtomicInteger value = new AtomicInteger();

    @Override
    public void set(int value) {
        this.value.setRelease(value);
    }

    @Override
    public int get() {
        return value.getAcquire();
    }

    @Override
    public int getPlain() {
        return value.getPlain();
    }
}
