package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.*;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class PrefilledRingBufferBuilder<T> extends AbstractPrefilledRingBufferBuilder<T> {
    PrefilledRingBufferBuilder(OverwritingPrefilledRingBufferBuilder<T> builder) {
        super(builder);
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
    protected RingBufferBuilder<T> blocking() {
        throw new AssertionError();
    }

    @Override
    protected RingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        throw new AssertionError();
    }

    @Override
    protected RingBufferBuilder<T> discarding() {
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    RingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
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
    public PrefilledRingBuffer<T> build() {
        return (PrefilledRingBuffer<T>) super.build();
    }
}
