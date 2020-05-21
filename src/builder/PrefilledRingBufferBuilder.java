package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.PrefilledRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Supplier;

import static eu.menzani.ringbuffer.BuilderProxy.*;

public class PrefilledRingBufferBuilder<T> extends AbstractPrefilledRingBufferBuilder<T> {
    PrefilledRingBufferBuilder(OverwritingPrefilledRingBufferBuilder<T> builder) {
        super(builder);
    }

    @Override
    public PrefilledRingBufferBuilder<T> fillWith(Supplier<? extends T> filler) {
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
                            return instantiateCopy(volatileBlockingPrefilledRingBuffer());
                        }
                        return volatileBlockingPrefilledRingBuffer(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(volatileDiscardingPrefilledRingBuffer());
                        }
                        return volatileDiscardingPrefilledRingBuffer(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(atomicReadBlockingPrefilledRingBuffer());
                        }
                        return atomicReadBlockingPrefilledRingBuffer(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(atomicReadDiscardingPrefilledRingBuffer());
                        }
                        return atomicReadDiscardingPrefilledRingBuffer(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(atomicWriteBlockingPrefilledRingBuffer());
                        }
                        return atomicWriteBlockingPrefilledRingBuffer(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(atomicWriteDiscardingPrefilledRingBuffer());
                        }
                        return atomicWriteDiscardingPrefilledRingBuffer(this);
                }
            case CONCURRENT:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(concurrentBlockingPrefilledRingBuffer());
                        }
                        return concurrentBlockingPrefilledRingBuffer(this);
                    case DISCARDING:
                        if (copyClass) {
                            return instantiateCopy(concurrentDiscardingPrefilledRingBuffer());
                        }
                        return concurrentDiscardingPrefilledRingBuffer(this);
                }
        }
        throw new AssertionError();
    }

    @Override
    public PrefilledRingBuffer<T> build() {
        return (PrefilledRingBuffer<T>) super.build();
    }
}
