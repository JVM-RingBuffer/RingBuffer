package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.marshalling.BuilderProxy.*;

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
    DirectMarshallingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(volatileDirectMarshallingRingBuffer());
                    }
                    return volatileDirectMarshallingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicReadDirectMarshallingRingBuffer());
                    }
                    return atomicReadDirectMarshallingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicWriteDirectMarshallingRingBuffer());
                    }
                    return atomicWriteDirectMarshallingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(concurrentDirectMarshallingRingBuffer());
                    }
                    return concurrentDirectMarshallingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
