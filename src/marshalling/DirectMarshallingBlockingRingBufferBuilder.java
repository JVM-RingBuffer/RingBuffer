package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.AbstractRingBufferBuilder;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class DirectMarshallingBlockingRingBufferBuilder extends AbstractDirectMarshallingRingBufferBuilder<DirectMarshallingBlockingRingBuffer> {
    DirectMarshallingBlockingRingBufferBuilder(DirectMarshallingRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder manyReaders() {
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
    public DirectMarshallingBlockingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder unsafe() {
        super.unsafe0();
        return this;
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder withByteArray(DirectByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected DirectMarshallingBlockingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileDirectMarshallingBlockingRingBuffer.class);
                    }
                    return new VolatileDirectMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadDirectMarshallingBlockingRingBuffer.class);
                    }
                    return new AtomicReadDirectMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteDirectMarshallingBlockingRingBuffer.class);
                    }
                    return new AtomicWriteDirectMarshallingBlockingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.BLOCKING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentDirectMarshallingBlockingRingBuffer.class);
                    }
                    return new ConcurrentDirectMarshallingBlockingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
