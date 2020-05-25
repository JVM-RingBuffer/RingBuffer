package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.object.PrefilledOverwritingRingBuffer;
import eu.menzani.ringbuffer.object.RingBuffer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Supplier;

import static eu.menzani.ringbuffer.object.BuilderProxy.*;

public class PrefilledOverwritingRingBufferBuilder<T> extends AbstractPrefilledRingBufferBuilder<T> {
    public PrefilledOverwritingRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public PrefilledOverwritingRingBufferBuilder<T> fillWith(Supplier<? extends T> filler) {
        super.fillWith0(filler);
        return this;
    }

    @Override
    public PrefilledOverwritingRingBufferBuilder<T> oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public PrefilledOverwritingRingBufferBuilder<T> manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public PrefilledOverwritingRingBufferBuilder<T> oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public PrefilledOverwritingRingBufferBuilder<T> manyReaders() {
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
    public PrefilledOverwritingRingBufferBuilder<T> waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public PrefilledOverwritingRingBufferBuilder<T> withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public PrefilledOverwritingRingBufferBuilder<T> copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    RingBuffer<T> create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(volatilePrefilledRingBuffer());
                    }
                    return volatilePrefilledRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicReadPrefilledRingBuffer());
                    }
                    return atomicReadPrefilledRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicWritePrefilledRingBuffer());
                    }
                    return atomicWritePrefilledRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(concurrentPrefilledRingBuffer());
                    }
                    return concurrentPrefilledRingBuffer(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public PrefilledOverwritingRingBuffer<T> build() {
        return (PrefilledOverwritingRingBuffer<T>) super.build();
    }
}