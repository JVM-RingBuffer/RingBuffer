package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.marshalling.BuilderProxy.*;

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
    AbstractRingBufferBuilder<?> blocking() {
        throw new AssertionError();
    }

    @Override
    AbstractRingBufferBuilder<?> blocking(BusyWaitStrategy busyWaitStrategy) {
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
    MarshallingBlockingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(volatileMarshallingBlockingRingBuffer());
                    }
                    return volatileMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicReadMarshallingBlockingRingBuffer());
                    }
                    return atomicReadMarshallingBlockingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicWriteMarshallingBlockingRingBuffer());
                    }
                    return atomicWriteMarshallingBlockingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(concurrentMarshallingBlockingRingBuffer());
                    }
                    return concurrentMarshallingBlockingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
