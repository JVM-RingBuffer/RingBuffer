package org.ringbuffer.object;

import eu.menzani.struct.Arrays;
import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.wait.BusyWaitStrategy;

public final class LockfreePrefilledRingBufferBuilder<T> extends AbstractRingBufferBuilder<LockfreePrefilledRingBuffer<T>> {
    private final int capacity;
    // All fields are copied in <init>(ObjectRingBufferBuilder<?>)

    LockfreePrefilledRingBufferBuilder(ObjectRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    @Override
    public LockfreePrefilledRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public LockfreePrefilledRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public LockfreePrefilledRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public LockfreePrefilledRingBufferBuilder<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    protected PrefilledRingBufferBuilder2<T> blocking() {
        throw new AssertionError();
    }

    @Override
    protected PrefilledRingBufferBuilder2<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        throw new AssertionError();
    }

    @Override
    protected LockfreePrefilledRingBufferBuilder<T> lockfree() {
        throw new AssertionError();
    }

    @Override
    public LockfreePrefilledRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public LockfreePrefilledRingBufferBuilder<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected LockfreePrefilledRingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.LOCKFREE) {
            switch (concurrency) {
                case VOLATILE:
                    return new LockfreeVolatilePrefilledRingBuffer<>(this);
                case ATOMIC_READ:
                    return new LockfreeAtomicReadPrefilledRingBuffer<>(this);
                case ATOMIC_WRITE:
                    return new LockfreeAtomicWritePrefilledRingBuffer<>(this);
                case CONCURRENT:
                    return new LockfreeConcurrentPrefilledRingBuffer<>(this);
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

    boolean[] getPositionNotModified() {
        boolean[] positionNotModified = new boolean[capacity];
        java.util.Arrays.fill(positionNotModified, true);
        return positionNotModified;
    }
}
