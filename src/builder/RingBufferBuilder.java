package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.classcopy.CopiedClass;
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
    boolean copyClass;
    // All fields are copied in AbstractPrefilledRingBufferBuilder.<init>(AbstractPrefilledRingBufferBuilder<T>)

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

    public abstract RingBufferBuilder<T> copyClass();

    void copyClass0() {
        copyClass = true;
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

    RingBuffer<T> instantiateCopy(Class<?> ringBufferClass) {
        return new CopiedClass<RingBuffer<T>>(ringBufferClass)
                .getConstructor(getClass())
                .call(this);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCapacityMinusOne() {
        return capacity - 1;
    }

    @SuppressWarnings("unchecked")
    public T[] getBuffer() {
        return (T[]) new Object[capacity];
    }

    public BusyWaitStrategy getWriteBusyWaitStrategy() {
        return writeBusyWaitStrategy;
    }

    public BusyWaitStrategy getReadBusyWaitStrategy() {
        return readBusyWaitStrategy;
    }

    public Integer newCursor() {
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
