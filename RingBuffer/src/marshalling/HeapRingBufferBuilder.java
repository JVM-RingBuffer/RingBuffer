package org.ringbuffer.marshalling;

import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.wait.BusyWaitStrategy;

public final class HeapRingBufferBuilder extends AbstractHeapRingBufferBuilder<HeapRingBuffer> {
    HeapRingBufferBuilder(HeapClearingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public HeapRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public HeapRingBufferBuilder manyReaders() {
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
    public HeapRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    protected HeapRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (type == RingBufferType.BLOCKING) {
            switch (concurrency) {
                case VOLATILE:
                    if (copyClass) {
                        return instantiateCopy(VolatileHeapBlockingRingBuffer.class);
                    }
                    return new VolatileHeapBlockingRingBuffer(this);
                case ATOMIC_READ:
                    if (copyClass) {
                        return instantiateCopy(AtomicReadHeapBlockingRingBuffer.class);
                    }
                    return new AtomicReadHeapBlockingRingBuffer(this);
                case ATOMIC_WRITE:
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteHeapBlockingRingBuffer.class);
                    }
                    return new AtomicWriteHeapBlockingRingBuffer(this);
                case CONCURRENT:
                    if (copyClass) {
                        return instantiateCopy(ConcurrentHeapBlockingRingBuffer.class);
                    }
                    return new ConcurrentHeapBlockingRingBuffer(this);
            }
        }
        throw new AssertionError();
    }
}
