package eu.menzani.ringbuffer.memory;

import eu.menzani.ringbuffer.concurrent.AtomicInt;

class VolatileInteger implements Integer {
    private final AtomicInt value = new AtomicInt();

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
