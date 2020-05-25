package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.marshalling.BuilderProxy.*;

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
    AbstractRingBufferBuilder<?> blocking() {
        throw new AssertionError();
    }

    @Override
    AbstractRingBufferBuilder<?> blocking(BusyWaitStrategy busyWaitStrategy) {
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
    DirectMarshallingBlockingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(volatileDirectMarshallingBlockingRingBuffer());
                    }
                    return volatileDirectMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicReadDirectMarshallingBlockingRingBuffer());
                    }
                    return atomicReadDirectMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicWriteDirectMarshallingBlockingRingBuffer());
                    }
                    return atomicWriteDirectMarshallingBlockingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(concurrentDirectMarshallingBlockingRingBuffer());
                    }
                    return concurrentDirectMarshallingBlockingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
