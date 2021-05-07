package org.ringbuffer.marshalling;

import org.ringbuffer.wait.BusyWaitStrategy;

public final class LockfreeHeapRingBufferBuilder extends AbstractHeapRingBufferBuilder<LockfreeHeapRingBuffer> {
    LockfreeHeapRingBufferBuilder(AbstractHeapRingBufferBuilder<?> builder) {
        super(builder);
    }

    @Override
    public LockfreeHeapRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public LockfreeHeapRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public LockfreeHeapRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public LockfreeHeapRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    protected HeapRingBufferBuilder blocking() {
        throw new AssertionError();
    }

    @Override
    protected HeapRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        throw new AssertionError();
    }

    @Override
    protected HeapRingBufferBuilder lockfree() {
        throw new AssertionError();
    }

    @Override
    public LockfreeHeapRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public LockfreeHeapRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected LockfreeHeapRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.LOCKFREE) {
            switch (concurrency) {
                case VOLATILE:
                    if (copyClass) {
                        return instantiateCopy(LockfreeVolatileHeapRingBuffer.class);
                    }
                    return new LockfreeVolatileHeapRingBuffer(this);
                case ATOMIC_READ:
                    if (copyClass) {
                        return instantiateCopy(LockfreeAtomicReadHeapRingBuffer.class);
                    }
                    return new LockfreeAtomicReadHeapRingBuffer(this);
                case ATOMIC_WRITE:
                    if (copyClass) {
                        return instantiateCopy(LockfreeAtomicWriteHeapRingBuffer.class);
                    }
                    return new LockfreeAtomicWriteHeapRingBuffer(this);
                case CONCURRENT:
                    if (copyClass) {
                        return instantiateCopy(LockfreeConcurrentHeapRingBuffer.class);
                    }
                    return new LockfreeConcurrentHeapRingBuffer(this);
            }
        }
        throw new AssertionError();
    }
}
