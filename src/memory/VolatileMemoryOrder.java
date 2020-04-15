package eu.menzani.ringbuffer.memory;

public class VolatileMemoryOrder implements MemoryOrder {
    @Override
    public Integer newInteger() {
        return new VolatileInteger();
    }
}
