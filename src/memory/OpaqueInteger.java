package eu.menzani.ringbuffer.memory;

import eu.menzani.ringbuffer.concurrent.AtomicInt;

class OpaqueInteger implements Integer {
    private final AtomicInt value = new AtomicInt();

    @Override
    public void set(int value) {
        this.value.setOpaque(value);
    }

    @Override
    public int get() {
        return value.getOpaque();
    }

    @Override
    public int getPlain() {
        return value.getPlain();
    }
}
