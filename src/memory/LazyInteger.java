package org.ringbuffer.memory;

import org.ringbuffer.concurrent.AtomicInt;

class LazyInteger implements Integer {
    private final AtomicInt value = new AtomicInt();

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
