package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Supplier;

public class PrefilledRingBufferBuilder<T> extends RingBufferBuilder<T> {
    private final Supplier<? extends T> filler;

    PrefilledRingBufferBuilder(int capacity, Supplier<? extends T> filler) {
        super(capacity);
        this.filler = filler;
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
    public PrefilledRingBufferBuilder<T> blocking() {
        super.blocking0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> discarding() {
        super.discarding0();
        return this;
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
    RingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case LOCAL:
                switch (type) {
                    case OVERWRITING:
                        return new LocalPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        return new LocalDiscardingPrefilledRingBuffer<>(this);
                }
            case VOLATILE:
                switch (type) {
                    case OVERWRITING:
                        return new VolatilePrefilledRingBuffer<>(this);
                    case BLOCKING:
                        return new VolatileBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        return new VolatileDiscardingPrefilledRingBuffer<>(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case OVERWRITING:
                        return new AtomicReadPrefilledRingBuffer<>(this);
                    case BLOCKING:
                        return new AtomicReadBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        return new AtomicReadDiscardingPrefilledRingBuffer<>(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case OVERWRITING:
                        return new AtomicWritePrefilledRingBuffer<>(this);
                    case BLOCKING:
                        return new AtomicWriteBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        return new AtomicWriteDiscardingPrefilledRingBuffer<>(this);
                }
            case CONCURRENT:
                switch (type) {
                    case OVERWRITING:
                        return new ConcurrentPrefilledRingBuffer<>(this);
                    case BLOCKING:
                        return new ConcurrentBlockingPrefilledRingBuffer<>(this);
                    case DISCARDING:
                        return new ConcurrentDiscardingPrefilledRingBuffer<>(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBuffer<T> build() {
        return (PrefilledRingBuffer<T>) super.build();
    }

    @Override
    T[] getBuffer() {
        T[] buffer = super.getBuffer();
        for (int i = 0; i < getCapacity(); i++) {
            buffer[i] = filler.get();
        }
        return buffer;
    }

    T getDummyElement() {
        return filler.get();
    }
}
