package org.ringbuffer.memory;

class OpaqueMemoryOrder implements MemoryOrder {
    @Override
    public Integer newInteger() {
        return new OpaqueInteger();
    }

    @Override
    public Long newLong() {
        return new OpaqueLong();
    }
}
