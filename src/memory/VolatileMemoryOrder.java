package eu.menzani.ringbuffer.memory;

public class VolatileMemoryOrder implements MemoryOrder {
    @Override
    public BooleanArray newBooleanArray(int capacity) {
        return new VolatileBooleanArray(capacity);
    }

    @Override
    public Integer newInteger() {
        return new VolatileInteger();
    }
}
