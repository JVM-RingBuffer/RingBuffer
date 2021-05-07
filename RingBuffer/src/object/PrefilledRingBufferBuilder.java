package org.ringbuffer.object;

import eu.menzani.object.ObjectFactory;
import org.ringbuffer.wait.BusyWaitStrategy;

public final class PrefilledRingBufferBuilder<T> extends AbstractPrefilledRingBufferBuilder<T> {
    PrefilledRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public PrefilledRingBufferBuilder<T> fillWith(ObjectFactory<T> filler) {
        super.fillWith0(filler);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> blocking() {
        super.blocking0();
        return new PrefilledRingBufferBuilder2<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder2<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new PrefilledRingBufferBuilder2<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder2<T> discarding() {
        super.discarding0();
        return new PrefilledRingBufferBuilder2<>(this);
    }

    @Override
    public LockfreePrefilledRingBufferBuilder<T> lockfree() {
        super.lockfree0();
        return new LockfreePrefilledRingBufferBuilder<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected ObjectRingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.CLEARING) {
            switch (concurrency) {
                case VOLATILE:
                    if (copyClass) {
                        return instantiateCopy(VolatilePrefilledRingBuffer.class);
                    }
                    return new VolatilePrefilledRingBuffer<>(this);
                case ATOMIC_READ:
                    if (copyClass) {
                        return instantiateCopy(AtomicReadPrefilledRingBuffer.class);
                    }
                    return new AtomicReadPrefilledRingBuffer<>(this);
                case ATOMIC_WRITE:
                    if (copyClass) {
                        return instantiateCopy(AtomicWritePrefilledRingBuffer.class);
                    }
                    return new AtomicWritePrefilledRingBuffer<>(this);
                case CONCURRENT:
                    if (copyClass) {
                        return instantiateCopy(ConcurrentPrefilledRingBuffer.class);
                    }
                    return new ConcurrentPrefilledRingBuffer<>(this);
            }
        }
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBuffer<T> build() {
        return (PrefilledRingBuffer<T>) super.build();
    }
}
