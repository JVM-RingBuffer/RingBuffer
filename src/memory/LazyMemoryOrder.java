package eu.menzani.ringbuffer.memory;

public class LazyMemoryOrder implements MemoryOrder {
    @Override
    public BooleanArray newBooleanArray(int capacity) {
        return new LazyBooleanArray(capacity);
    }

    @Override
    public Integer newInteger() {
        return new LazyInteger();
    }
}
