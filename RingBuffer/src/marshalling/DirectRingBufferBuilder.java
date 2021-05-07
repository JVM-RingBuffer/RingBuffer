package org.ringbuffer.marshalling;

import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.wait.BusyWaitStrategy;

public final class DirectRingBufferBuilder extends AbstractDirectRingBufferBuilder<DirectRingBuffer> {
    DirectRingBufferBuilder(DirectClearingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public DirectRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public DirectRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public DirectRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public DirectRingBufferBuilder manyReaders() {
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
    protected AbstractRingBufferBuilder<?> lockfree() {
        throw new AssertionError();
    }

    @Override
    public DirectRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public DirectRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected DirectRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.BLOCKING) {
            switch (concurrency) {
                case VOLATILE:
                    if (copyClass) {
                        return instantiateCopy(VolatileDirectBlockingRingBuffer.class);
                    }
                    return new VolatileDirectBlockingRingBuffer(this);
                case ATOMIC_READ:
                    if (copyClass) {
                        return instantiateCopy(AtomicReadDirectBlockingRingBuffer.class);
                    }
                    return new AtomicReadDirectBlockingRingBuffer(this);
                case ATOMIC_WRITE:
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteDirectBlockingRingBuffer.class);
                    }
                    return new AtomicWriteDirectBlockingRingBuffer(this);
                case CONCURRENT:
                    if (copyClass) {
                        return instantiateCopy(ConcurrentDirectBlockingRingBuffer.class);
                    }
                    return new ConcurrentDirectBlockingRingBuffer(this);
            }
        }
        throw new AssertionError();
    }
}
