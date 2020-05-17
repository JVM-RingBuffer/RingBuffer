package eu.menzani.ringbuffer.memory;

class VolatileMemoryOrder implements MemoryOrder {
    @Override
    public Integer newInteger() {
        return new VolatileInteger();
    }
}
