package eu.menzani.ringbuffer.memory;

import eu.menzani.ringbuffer.concurrent.AtomicLong;

class OpaqueLong implements Long {
    private final AtomicLong value = new AtomicLong();

    @Override
    public void set(long value) {
        this.value.setOpaque(value);
    }

    @Override
    public long get() {
        return value.getOpaque();
    }

    @Override
    public long getPlain() {
        return value.getPlain();
    }
}
