package eu.menzani.ringbuffer.memory;

class PlainMemoryOrder implements MemoryOrder {
    @Override
    public Integer newInteger() {
        return new PlainInteger();
    }
}
