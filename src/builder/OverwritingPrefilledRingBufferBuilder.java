package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.AtomicReadPrefilledRingBuffer;
import eu.menzani.ringbuffer.AtomicWritePrefilledRingBuffer;
import eu.menzani.ringbuffer.ConcurrentPrefilledRingBuffer;
import eu.menzani.ringbuffer.OverwritingPrefilledRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.VolatilePrefilledRingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Supplier;

public class OverwritingPrefilledRingBufferBuilder<T> extends AbstractPrefilledRingBufferBuilder<T> {
    public OverwritingPrefilledRingBufferBuilder(int capacity, Supplier<? extends T> filler) {
        super(capacity, filler);
    }

    @Override
    public OverwritingPrefilledRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public OverwritingPrefilledRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public OverwritingPrefilledRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public OverwritingPrefilledRingBufferBuilder<T> manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public PrefilledRingBufferBuilder<T> blocking() {
        super.blocking0();
        return new PrefilledRingBufferBuilder<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder<T> blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new PrefilledRingBufferBuilder<>(this);
    }

    @Override
    public PrefilledRingBufferBuilder<T> discarding() {
        super.discarding0();
        return new PrefilledRingBufferBuilder<>(this);
    }

    @Override
    public OverwritingPrefilledRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public OverwritingPrefilledRingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public OverwritingPrefilledRingBufferBuilder<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    RingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(VolatilePrefilledRingBuffer.class);
                    }
                    return new VolatilePrefilledRingBuffer<>(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadPrefilledRingBuffer.class);
                    }
                    return new AtomicReadPrefilledRingBuffer<>(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWritePrefilledRingBuffer.class);
                    }
                    return new AtomicWritePrefilledRingBuffer<>(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentPrefilledRingBuffer.class);
                    }
                    return new ConcurrentPrefilledRingBuffer<>(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public OverwritingPrefilledRingBuffer<T> build() {
        return (OverwritingPrefilledRingBuffer<T>) super.build();
    }
}
