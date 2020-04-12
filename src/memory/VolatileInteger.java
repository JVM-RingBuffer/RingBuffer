package eu.menzani.ringbuffer.memory;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileInteger implements Integer {
    private final AtomicInteger value = new AtomicInteger();

    @Override
    public void set(int value) {
        this.value.set(value);
    }

    @Override
    public int getAndDecrement() {
        return value.getAndDecrement();
    }

    @Override
    public int get() {
        return value.get();
    }

    @Override
    public int getPlain() {
        return value.getPlain();
    }
}
