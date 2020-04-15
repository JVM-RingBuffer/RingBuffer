package eu.menzani.ringbuffer.memory;

public interface MemoryOrder {
    Integer newInteger();

    MemoryOrder LAZY = new LazyMemoryOrder();
    MemoryOrder VOLATILE = new VolatileMemoryOrder();
}
