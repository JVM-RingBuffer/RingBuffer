package eu.menzani.ringbuffer.memory;

public interface MemoryOrder {
    Integer newInteger();

    MemoryOrder PLAIN = new PlainMemoryOrder();
    MemoryOrder OPAQUE = new OpaqueMemoryOrder();
    MemoryOrder LAZY = new LazyMemoryOrder();
    MemoryOrder VOLATILE = new VolatileMemoryOrder();
}
