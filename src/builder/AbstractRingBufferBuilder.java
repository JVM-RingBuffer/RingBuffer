package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.classcopy.CopiedClass;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;
import eu.menzani.ringbuffer.wait.HintBusyWaitStrategy;

abstract class AbstractRingBufferBuilder<T> {
    Boolean oneWriter;
    Boolean oneReader;
    RingBufferType type = RingBufferType.OVERWRITING;
    BusyWaitStrategy writeBusyWaitStrategy;
    BusyWaitStrategy readBusyWaitStrategy = HintBusyWaitStrategy.getDefault();
    MemoryOrder memoryOrder = MemoryOrder.LAZY;
    boolean copyClass;
    // All fields are copied in <init>(AbstractRingBufferBuilder<T>)

    AbstractRingBufferBuilder() {}

    AbstractRingBufferBuilder(AbstractRingBufferBuilder<?> builder) {
        oneWriter = builder.oneWriter;
        oneReader = builder.oneReader;
        type = builder.type;
        writeBusyWaitStrategy = builder.writeBusyWaitStrategy;
        readBusyWaitStrategy = builder.readBusyWaitStrategy;
        memoryOrder = builder.memoryOrder;
        copyClass = builder.copyClass;
    }

    public abstract AbstractRingBufferBuilder<T> oneWriter();

    void oneWriter0() {
        oneWriter = true;
    }

    public abstract AbstractRingBufferBuilder<T> manyWriters();

    void manyWriters0() {
        oneWriter = false;
    }

    public abstract AbstractRingBufferBuilder<T> oneReader();

    void oneReader0() {
        oneReader = true;
    }

    public abstract AbstractRingBufferBuilder<T> manyReaders();

    void manyReaders0() {
        oneReader = false;
    }

    abstract AbstractRingBufferBuilder<?> blocking();

    void blocking0() {
        blocking0(HintBusyWaitStrategy.getDefault());
    }

    abstract AbstractRingBufferBuilder<?> blocking(BusyWaitStrategy busyWaitStrategy);

    void blocking0(BusyWaitStrategy busyWaitStrategy) {
        type = RingBufferType.BLOCKING;
        writeBusyWaitStrategy = busyWaitStrategy;
    }

    public abstract AbstractRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy);

    void waitingWith0(BusyWaitStrategy busyWaitStrategy) {
        readBusyWaitStrategy = busyWaitStrategy;
    }

    public abstract AbstractRingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder);

    void withMemoryOrder0(MemoryOrder memoryOrder) {
        this.memoryOrder = memoryOrder;
    }

    /**
     * A copy of the underlying implementation will be instantiated to allow inlining of polymorphic calls.
     */
    public abstract AbstractRingBufferBuilder<T> copyClass();

    void copyClass0() {
        copyClass = true;
    }

    public T build() {
        validate();

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

    void validate() {
        if (oneReader == null && oneWriter == null) {
            throw new IllegalStateException("You must call either oneReader() or manyReaders(), and oneWriter() or manyWriters().");
        }
        if (oneReader == null) {
            throw new IllegalStateException("You must call either oneReader() or manyReaders().");
        }
        if (oneWriter == null) {
            throw new IllegalStateException("You must call either oneWriter() or manyWriters().");
        }
    }

    abstract T create(RingBufferConcurrency concurrency, RingBufferType type);

    T instantiateCopy(Class<?> ringBufferClass) {
        return new CopiedClass<T>(ringBufferClass)
                .getConstructor(getClass())
                .call(this);
    }

    BusyWaitStrategy getWriteBusyWaitStrategy() {
        return writeBusyWaitStrategy;
    }

    BusyWaitStrategy getReadBusyWaitStrategy() {
        return readBusyWaitStrategy;
    }
}
