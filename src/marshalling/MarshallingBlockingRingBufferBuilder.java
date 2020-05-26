package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.AbstractRingBufferBuilder;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class MarshallingBlockingRingBufferBuilder extends AbstractMarshallingRingBufferBuilder<MarshallingBlockingRingBuffer> {
    MarshallingBlockingRingBufferBuilder(MarshallingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public MarshallingBlockingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder manyReaders() {
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
    public MarshallingBlockingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder unsafe() {
        super.unsafe0();
        return this;
    }

    @Override
    public MarshallingBlockingRingBufferBuilder withByteArray(ByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected MarshallingBlockingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileMarshallingBlockingRingBuffer.class);
                    }
                    return new VolatileMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadMarshallingBlockingRingBuffer.class);
                    }
                    return new AtomicReadMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteMarshallingBlockingRingBuffer.class);
                    }
                    return new AtomicWriteMarshallingBlockingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentMarshallingBlockingRingBuffer.class);
                    }
                    return new ConcurrentMarshallingBlockingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
