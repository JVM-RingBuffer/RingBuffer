package eu.menzani.ringbuffer.memory;

class OpaqueMemoryOrder implements MemoryOrder {
    @Override
    public Integer newInteger() {
        return new OpaqueInteger();
    }
}
