package eu.menzani.ringbuffer.memory;

public class LazyMemoryOrder implements MemoryOrder {
    @Override
    public Integer newInteger() {
        return new LazyInteger();
    }
}
