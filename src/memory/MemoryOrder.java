package eu.menzani.ringbuffer.memory;

public interface MemoryOrder {
    BooleanArray newBooleanArray(int capacity);

    Integer newInteger();

    MemoryOrder LAZY = new LazyMemoryOrder();
    MemoryOrder VOLATILE = new VolatileMemoryOrder();
}
