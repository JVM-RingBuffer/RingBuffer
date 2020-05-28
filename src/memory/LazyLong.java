package org.ringbuffer.memory;

import org.ringbuffer.concurrent.AtomicLong;

class LazyLong implements Long {
    private final AtomicLong value = new AtomicLong();

    @Override
    public void set(long value) {
        this.value.setRelease(value);
    }

    @Override
    public long get() {
        return value.getAcquire();
    }

    @Override
    public long getPlain() {
        return value.getPlain();
    }
}
