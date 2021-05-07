package org.ringbuffer.marshalling;

import org.ringbuffer.wait.BusyWaitStrategy;

public final class LockfreeDirectRingBufferBuilder extends AbstractDirectRingBufferBuilder<LockfreeDirectRingBuffer> {
    LockfreeDirectRingBufferBuilder(AbstractDirectRingBufferBuilder<?> builder) {
        super(builder);
    }

    @Override
    public LockfreeDirectRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public LockfreeDirectRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public LockfreeDirectRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public LockfreeDirectRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    protected DirectRingBufferBuilder blocking() {
        throw new AssertionError();
    }

    @Override
    protected DirectRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        throw new AssertionError();
    }

    @Override
    protected DirectRingBufferBuilder lockfree() {
        throw new AssertionError();
    }

    @Override
    public LockfreeDirectRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public LockfreeDirectRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected LockfreeDirectRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.LOCKFREE) {
            switch (concurrency) {
                case VOLATILE:
                    if (copyClass) {
                        return instantiateCopy(LockfreeVolatileDirectRingBuffer.class);
                    }
                    return new LockfreeVolatileDirectRingBuffer(this);
                case ATOMIC_READ:
                    if (copyClass) {
                        return instantiateCopy(LockfreeAtomicReadDirectRingBuffer.class);
                    }
                    return new LockfreeAtomicReadDirectRingBuffer(this);
                case ATOMIC_WRITE:
                    if (copyClass) {
                        return instantiateCopy(LockfreeAtomicWriteDirectRingBuffer.class);
                    }
                    return new LockfreeAtomicWriteDirectRingBuffer(this);
                case CONCURRENT:
                    if (copyClass) {
                        return instantiateCopy(LockfreeConcurrentDirectRingBuffer.class);
                    }
                    return new LockfreeConcurrentDirectRingBuffer(this);
            }
        }
        throw new AssertionError();
    }
}
