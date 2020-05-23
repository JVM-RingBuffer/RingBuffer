package eu.menzani.ringbuffer.memory;

import eu.menzani.ringbuffer.concurrent.AtomicLong;

class VolatileLong implements Long {
    private final AtomicLong value = new AtomicLong();

    @Override
    public void set(long value) {
        this.value.setVolatile(value);
    }

    @Override
    public long get() {
        return value.getVolatile();
    }

    @Override
    public long getPlain() {
        return value.getPlain();
    }
}
