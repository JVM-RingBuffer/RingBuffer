package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.HintBusyWaitStrategy;

abstract class RingBufferBuilder<T> {
    final int capacity;
    Boolean oneWriter;
    Boolean oneReader;
    RingBufferType type = RingBufferType.OVERWRITING;
    BusyWaitStrategy writeBusyWaitStrategy;
    BusyWaitStrategy readBusyWaitStrategy = HintBusyWaitStrategy.getDefault();
    MemoryOrder memoryOrder = MemoryOrder.LAZY;

    RingBufferBuilder(int capacity) {
        Assume.notLesser(capacity, 2);
        this.capacity = capacity;
    }

    public abstract RingBufferBuilder<T> oneWriter();

    void oneWriter0() {
        oneWriter = true;
    }

    public abstract RingBufferBuilder<T> manyWriters();

    void manyWriters0() {
        oneWriter = false;
    }

    public abstract RingBufferBuilder<T> oneReader();

    void oneReader0() {
        oneReader = true;
    }

    public abstract RingBufferBuilder<T> manyReaders();

    void manyReaders0() {
        oneReader = false;
    }

    protected abstract RingBufferBuilder<T> blocking();

    void blocking0() {
        blocking0(HintBusyWaitStrategy.getDefault());
    }

    protected abstract RingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy);

    void blocking0(BusyWaitStrategy busyWaitStrategy) {
        type = RingBufferType.BLOCKING;
        writeBusyWaitStrategy = busyWaitStrategy;
    }

    protected abstract RingBufferBuilder<T> discarding();

    void discarding0() {
        type = RingBufferType.DISCARDING;
    }

    public abstract RingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy);

    void waitingWith0(BusyWaitStrategy busyWaitStrategy) {
        readBusyWaitStrategy = busyWaitStrategy;
    }

    public abstract RingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder);

    void withMemoryOrder0(MemoryOrder memoryOrder) {
        this.memoryOrder = memoryOrder;
    }

    RingBuffer<T> build() {
        if (oneReader == null && oneWriter == null) {
            throw new IllegalStateException("You must call either oneReader() or manyReaders(), and oneWriter() or manyWriters().");
        }
        if (oneReader == null) {
            throw new IllegalStateException("You must call either oneReader() or manyReaders().");
        }
        if (oneWriter == null) {
            throw new IllegalStateException("You must call either oneWriter() or manyWriters().");
        }
        RingBufferConcurrency concurrency;
        if (oneReader) {
            if (oneWriter) {
                concurrency = RingBufferConcurrency.VOLATILE;
            } else {
                concurrency = RingBufferConcurrency.ATOMIC_WRITE;
            }
        } else if (oneWriter) {
            concurrency = RingBufferConcurrency.ATOMIC_READ;
        } else {
            concurrency = RingBufferConcurrency.CONCURRENT;
        }
        return create(concurrency, type);
    }

    abstract RingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type);

    int getCapacity() {
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    @SuppressWarnings("unchecked")
    T[] getBuffer() {
        return (T[]) new Object[capacity];
    }

    BusyWaitStrategy getWriteBusyWaitStrategy() {
        return writeBusyWaitStrategy;
    }

    BusyWaitStrategy getReadBusyWaitStrategy() {
        return readBusyWaitStrategy;
    }

    Integer newCursor() {
        return memoryOrder.newInteger();
    }

    enum RingBufferType {
        OVERWRITING,
        BLOCKING,
        DISCARDING
    }

    enum RingBufferConcurrency {
        VOLATILE,
        ATOMIC_READ,
        ATOMIC_WRITE,
        CONCURRENT
    }
}
