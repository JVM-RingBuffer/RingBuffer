package org.ringbuffer.object;

import eu.menzani.struct.Arrays;
import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.wait.BusyWaitStrategy;

public final class LockfreeRingBufferBuilder<T> extends AbstractRingBufferBuilder<LockfreeRingBuffer<T>> {
    private final int capacity;
    // All fields are copied in <init>(ObjectRingBufferBuilder<?>)

    LockfreeRingBufferBuilder(ObjectRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    @Override
    public LockfreeRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public LockfreeRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public LockfreeRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public LockfreeRingBufferBuilder<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    protected LockfreeRingBufferBuilder<T> blocking() {
        throw new AssertionError();
    }

    @Override
    protected LockfreeRingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        throw new AssertionError();
    }

    @Override
    protected LockfreeRingBufferBuilder<T> lockfree() {
        throw new AssertionError();
    }

    @Override
    public LockfreeRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public LockfreeRingBufferBuilder<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected LockfreeRingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.LOCKFREE) {
            switch (concurrency) {
                case VOLATILE:
                    return new LockfreeVolatileRingBuffer<>(this);
                case ATOMIC_READ:
                    return new LockfreeAtomicReadRingBuffer<>(this);
                case ATOMIC_WRITE:
                    return new LockfreeAtomicWriteRingBuffer<>(this);
                case CONCURRENT:
                    return new LockfreeConcurrentRingBuffer<>(this);
            }
        }
        throw new AssertionError();
    }

    @Override
    protected BusyWaitStrategy getWriteBusyWaitStrategy() {
        return super.getWriteBusyWaitStrategy();
    }

    @Override
    protected BusyWaitStrategy getReadBusyWaitStrategy() {
        return super.getReadBusyWaitStrategy();
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    T[] getBuffer() {
        return Arrays.allocateGeneric(capacity);
    }
}
