package eu.menzani.ringbuffer;

import java.util.concurrent.atomic.AtomicInteger;

class LazyVolatileInteger {
    private final AtomicInteger value;

    LazyVolatileInteger(int value) {
        this.value = new AtomicInteger(value);
    }

    void set(int value) {
        this.value.setRelease(value);
    }

    int get() {
        return value.getAcquire();
    }

    int getPlain() {
        return value.getPlain();
    }
}
