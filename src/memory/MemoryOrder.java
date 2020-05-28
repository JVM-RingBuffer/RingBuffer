package org.ringbuffer.memory;

public interface MemoryOrder {
    Integer newInteger();

    Long newLong();

    MemoryOrder PLAIN = new PlainMemoryOrder();
    MemoryOrder OPAQUE = new OpaqueMemoryOrder();
    MemoryOrder LAZY = new LazyMemoryOrder();
    MemoryOrder VOLATILE = new VolatileMemoryOrder();
}
