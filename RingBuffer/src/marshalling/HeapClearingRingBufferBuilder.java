package org.ringbuffer.marshalling;

import org.ringbuffer.wait.BusyWaitStrategy;

public final class HeapClearingRingBufferBuilder extends AbstractHeapRingBufferBuilder<HeapClearingRingBuffer> {
    HeapClearingRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public HeapClearingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public HeapClearingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public HeapClearingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public HeapClearingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder blocking() {
        super.blocking0();
        return new HeapRingBufferBuilder(this);
    }

    @Override
    public HeapRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new HeapRingBufferBuilder(this);
    }

    @Override
    public LockfreeHeapRingBufferBuilder lockfree() {
        super.lockfree0();
        return new LockfreeHeapRingBufferBuilder(this);
    }

    @Override
    public HeapClearingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapClearingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected HeapClearingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.CLEARING) {
            switch (concurrency) {
                case VOLATILE:
                    if (copyClass) {
                        return instantiateCopy(VolatileHeapRingBuffer.class);
                    }
                    return new VolatileHeapRingBuffer(this);
                case ATOMIC_READ:
                    if (copyClass) {
                        return instantiateCopy(AtomicReadHeapRingBuffer.class);
                    }
                    return new AtomicReadHeapRingBuffer(this);
                case ATOMIC_WRITE:
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteHeapRingBuffer.class);
                    }
                    return new AtomicWriteHeapRingBuffer(this);
                case CONCURRENT:
                    if (copyClass) {
                        return instantiateCopy(ConcurrentHeapRingBuffer.class);
                    }
                    return new ConcurrentHeapRingBuffer(this);
            }
        }
        throw new AssertionError();
    }
}
