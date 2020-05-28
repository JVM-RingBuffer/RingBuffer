package org.ringbuffer.marshalling;

import org.ringbuffer.memory.MemoryOrder;
import org.ringbuffer.wait.BusyWaitStrategy;

public class MarshallingRingBufferBuilder extends AbstractMarshallingRingBufferBuilder<MarshallingRingBuffer> {
    public MarshallingRingBufferBuilder(int capacity) {
        super(capacity);
    }

    @Override
    public MarshallingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public MarshallingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public MarshallingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public MarshallingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder blocking() {
        super.blocking0();
        return new MarshallingBlockingRingBufferBuilder(this);
    }

    @Override
    public MarshallingBlockingRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new MarshallingBlockingRingBufferBuilder(this);
    }

    @Override
    public MarshallingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public MarshallingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public MarshallingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public MarshallingRingBufferBuilder unsafe() {
        super.unsafe0();
        return this;
    }

    @Override
    public MarshallingRingBufferBuilder withByteArray(ByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected MarshallingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileMarshallingRingBuffer.class);
                    }
                    return new VolatileMarshallingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadMarshallingRingBuffer.class);
                    }
                    return new AtomicReadMarshallingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteMarshallingRingBuffer.class);
                    }
                    return new AtomicWriteMarshallingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentMarshallingRingBuffer.class);
                    }
                    return new ConcurrentMarshallingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}