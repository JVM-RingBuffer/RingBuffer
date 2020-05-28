package org.ringbuffer.memory;

class VolatileMemoryOrder implements MemoryOrder {
    @Override
    public Integer newInteger() {
        return new VolatileInteger();
    }

    @Override
    public Long newLong() {
        return new VolatileLong();
    }
}
