package org.ringbuffer.marshalling;

import org.ringbuffer.wait.BusyWaitStrategy;

public final class DirectClearingRingBufferBuilder extends AbstractDirectRingBufferBuilder<DirectClearingRingBuffer> {
    DirectClearingRingBufferBuilder(long capacity) {
        super(capacity);
    }

    @Override
    public DirectClearingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public DirectClearingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public DirectClearingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public DirectClearingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public DirectRingBufferBuilder blocking() {
        super.blocking0();
        return new DirectRingBufferBuilder(this);
    }

    @Override
    public DirectRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new DirectRingBufferBuilder(this);
    }

    @Override
    public LockfreeDirectRingBufferBuilder lockfree() {
        super.lockfree0();
        return new LockfreeDirectRingBufferBuilder(this);
    }

    @Override
    public DirectClearingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public DirectClearingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected DirectClearingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.CLEARING) {
            switch (concurrency) {
                case VOLATILE:
                    if (copyClass) {
                        return instantiateCopy(VolatileDirectRingBuffer.class);
                    }
                    return new VolatileDirectRingBuffer(this);
                case ATOMIC_READ:
                    if (copyClass) {
                        return instantiateCopy(AtomicReadDirectRingBuffer.class);
                    }
                    return new AtomicReadDirectRingBuffer(this);
                case ATOMIC_WRITE:
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteDirectRingBuffer.class);
                    }
                    return new AtomicWriteDirectRingBuffer(this);
                case CONCURRENT:
                    if (copyClass) {
                        return instantiateCopy(ConcurrentDirectRingBuffer.class);
                    }
                    return new ConcurrentDirectRingBuffer(this);
            }
        }
        throw new AssertionError();
    }
}
