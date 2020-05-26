package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class DirectMarshallingRingBufferBuilder extends AbstractDirectMarshallingRingBufferBuilder<DirectMarshallingRingBuffer> {
    public DirectMarshallingRingBufferBuilder(long capacity) {
        super(capacity);
    }

    @Override
    public DirectMarshallingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder blocking() {
        super.blocking0();
        return new DirectMarshallingBlockingRingBufferBuilder(this);
    }

    @Override
    public DirectMarshallingBlockingRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new DirectMarshallingBlockingRingBufferBuilder(this);
    }

    @Override
    public DirectMarshallingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder unsafe() {
        super.unsafe0();
        return this;
    }

    @Override
    public DirectMarshallingRingBufferBuilder withByteArray(DirectByteArray.Factory factory) {
        super.withByteArray0(factory);
        return this;
    }

    @Override
    protected DirectMarshallingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(VolatileDirectMarshallingRingBuffer.class);
                    }
                    return new VolatileDirectMarshallingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicReadDirectMarshallingRingBuffer.class);
                    }
                    return new AtomicReadDirectMarshallingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(AtomicWriteDirectMarshallingRingBuffer.class);
                    }
                    return new AtomicWriteDirectMarshallingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(ConcurrentDirectMarshallingRingBuffer.class);
                    }
                    return new ConcurrentDirectMarshallingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
