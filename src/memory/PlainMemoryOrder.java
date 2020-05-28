package org.ringbuffer.memory;

class PlainMemoryOrder implements MemoryOrder {
    @Override
    public Integer newInteger() {
        return new PlainInteger();
    }

    @Override
    public Long newLong() {
        return new PlainLong();
    }
}
