package eu.menzani.ringbuffer;

import java.util.concurrent.atomic.AtomicInteger;

class LazyVolatileInteger {
    private final AtomicInteger value = new AtomicInteger();

    void set(int value) {
        this.value.setRelease(value);
    }

    int get() {
        return value.getAcquire();
    }

    int getPlain() {
        return value.getPlain();
    }

    int getAndDecrement() {
        return value.getAndDecrement();
    }
}
