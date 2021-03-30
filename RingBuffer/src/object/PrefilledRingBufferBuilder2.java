package org.ringbuffer.object;

import eu.menzani.object.ObjectFactory;
import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.wait.BusyWaitStrategy;

public final class PrefilledRingBufferBuilder2<T> extends AbstractPrefilledRingBufferBuilder<T> {
    PrefilledRingBufferBuilder2(PrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }

    @Override
    public PrefilledRingBufferBuilder2<T> fillWith(ObjectFactory<T> filler) {
        super.fillWith0(filler);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    protected AbstractRingBufferBuilder<?> blocking() {
        throw new AssertionError();
    }

    @Override
    protected AbstractRingBufferBuilder<?> blocking(BusyWaitStrategy busyWaitStrategy) {
        throw new AssertionError();
    }

    @Override
    ObjectRingBufferBuilder<?> discarding() {
        throw new AssertionError();
    }

    @Override
    protected AbstractRingBufferBuilder<?> lockfree() {
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBufferBuilder2<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder2<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected ObjectRingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(VolatileBlockingPrefilledRingBuffer.class);
                        }
                        return new VolatileBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(VolatileDiscardingPrefilledRingBuffer.class);
                        }
                        return new VolatileDiscardingPrefilledRingBuffer<>(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadBlockingPrefilledRingBuffer.class);
                        }
                        return new AtomicReadBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadDiscardingPrefilledRingBuffer.class);
                        }
                        return new AtomicReadDiscardingPrefilledRingBuffer<>(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteBlockingPrefilledRingBuffer.class);
                        }
                        return new AtomicWriteBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteDiscardingPrefilledRingBuffer.class);
                        }
                        return new AtomicWriteDiscardingPrefilledRingBuffer<>(this);
                }
            case CONCURRENT:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentBlockingPrefilledRingBuffer.class);
                        }
                        return new ConcurrentBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentDiscardingPrefilledRingBuffer.class);
                        }
                        return new ConcurrentDiscardingPrefilledRingBuffer<>(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBuffer2<T> build() {
        return (PrefilledRingBuffer2<T>) super.build();
    }
}
