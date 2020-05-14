package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class EmptyRingBufferBuilder<T> extends RingBufferBuilder<T> {
    private boolean gcEnabled;

    EmptyRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public EmptyRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> blocking() {
        super.blocking0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> discarding() {
        super.discarding0();
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public EmptyRingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    public EmptyRingBufferBuilder<T> withGC() {
        gcEnabled = true;
        return this;
    }

    @Override
    RingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case OVERWRITING:
                        if (gcEnabled) {
                            return new VolatileGCRingBuffer<>(this);
                        }
                        return new VolatileRingBuffer<>(this);
                    case BLOCKING:
                        if (gcEnabled) {
                            return new VolatileBlockingGCRingBuffer<>(this);
                        }
                        return new VolatileBlockingRingBuffer<>(this);
                    case DISCARDING:
                        if (gcEnabled) {
                            return new VolatileDiscardingGCRingBuffer<>(this);
                        }
                        return new VolatileDiscardingRingBuffer<>(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case OVERWRITING:
                        if (gcEnabled) {
                            return new AtomicReadGCRingBuffer<>(this);
                        }
                        return new AtomicReadRingBuffer<>(this);
                    case BLOCKING:
                        if (gcEnabled) {
                            return new AtomicReadBlockingGCRingBuffer<>(this);
                        }
                        return new AtomicReadBlockingRingBuffer<>(this);
                    case DISCARDING:
                        if (gcEnabled) {
                            return new AtomicReadDiscardingGCRingBuffer<>(this);
                        }
                        return new AtomicReadDiscardingRingBuffer<>(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case OVERWRITING:
                        if (gcEnabled) {
                            return new AtomicWriteGCRingBuffer<>(this);
                        }
                        return new AtomicWriteRingBuffer<>(this);
                    case BLOCKING:
                        if (gcEnabled) {
                            return new AtomicWriteBlockingGCRingBuffer<>(this);
                        }
                        return new AtomicWriteBlockingRingBuffer<>(this);
                    case DISCARDING:
                        if (gcEnabled) {
                            return new AtomicWriteDiscardingGCRingBuffer<>(this);
                        }
                        return new AtomicWriteDiscardingRingBuffer<>(this);
                }
            case CONCURRENT:
                switch (type) {
                    case OVERWRITING:
                        if (gcEnabled) {
                            return new ConcurrentGCRingBuffer<>(this);
                        }
                        return new ConcurrentRingBuffer<>(this);
                    case BLOCKING:
                        if (gcEnabled) {
                            return new ConcurrentBlockingGCRingBuffer<>(this);
                        }
                        return new ConcurrentBlockingRingBuffer<>(this);
                    case DISCARDING:
                        if (gcEnabled) {
                            return new ConcurrentDiscardingGCRingBuffer<>(this);
                        }
                        return new ConcurrentDiscardingRingBuffer<>(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public EmptyRingBuffer<T> build() {
        return (EmptyRingBuffer<T>) super.build();
    }
}
