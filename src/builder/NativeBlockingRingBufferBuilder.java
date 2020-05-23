package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.NativeBlockingRingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class NativeBlockingRingBufferBuilder extends AbstractNativeRingBufferBuilder<NativeBlockingRingBuffer> {
    NativeBlockingRingBufferBuilder(NativeRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public NativeBlockingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder manyReaders() {
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
    public NativeBlockingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder unsafe() {
        super.unsafe0();
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder asserting() {
        super.asserting0();
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder notReporting() {
        super.notReporting0();
        return this;
    }

    @Override
    NativeBlockingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (copyClass) {
            return instantiateCopy(null);
        }
        return null;
    }
}
