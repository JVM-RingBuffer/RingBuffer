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
        switch (concurrency) {
            case VOLATILE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(VolatileHeapBlockingRingBuffer.class);
                        }
                        return new VolatileHeapBlockingRingBuffer(this);
                    case LOCKFREE:
                        return new LockfreeVolatileHeapRingBuffer(this);
                }
            case ATOMIC_READ:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicReadHeapBlockingRingBuffer.class);
                        }
                        return new AtomicReadHeapBlockingRingBuffer(this);
                    case LOCKFREE:
                        return new LockfreeAtomicReadHeapRingBuffer(this);
                }
            case ATOMIC_WRITE:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(AtomicWriteHeapBlockingRingBuffer.class);
                        }
                        return new AtomicWriteHeapBlockingRingBuffer(this);
                    case LOCKFREE:
                        return new LockfreeAtomicWriteHeapRingBuffer(this);
                }
            case CONCURRENT:
                switch (type) {
                    case BLOCKING:
                        if (copyClass) {
                            return instantiateCopy(ConcurrentHeapBlockingRingBuffer.class);
                        }
                        return new ConcurrentHeapBlockingRingBuffer(this);
                    case LOCKFREE:
                        return new LockfreeConcurrentHeapRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
