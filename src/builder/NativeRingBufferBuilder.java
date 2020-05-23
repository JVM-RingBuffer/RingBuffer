package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.NativeRingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class NativeRingBufferBuilder extends AbstractNativeRingBufferBuilder<NativeRingBuffer> {
    public NativeRingBufferBuilder(long capacity) {
        super(capacity);
    }

    @Override
    public NativeRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public NativeRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public NativeRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public NativeRingBufferBuilder manyReaders() {
        super.manyReaders0();
        return this;
    }

    @Override
    public NativeBlockingRingBufferBuilder blocking() {
        super.blocking0();
        return new NativeBlockingRingBufferBuilder(this);
    }

    @Override
    public NativeBlockingRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new NativeBlockingRingBufferBuilder(this);
    }

    @Override
    public NativeRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public NativeRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public NativeRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public NativeRingBufferBuilder unsafe() {
        super.unsafe0();
        return this;
    }

    @Override
    public NativeRingBufferBuilder asserting() {
        super.asserting0();
        return this;
    }

    @Override
    public NativeRingBufferBuilder notReporting() {
        super.notReporting0();
        return this;
    }

    @Override
    NativeRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (copyClass) {
            return instantiateCopy(null);
        }
        return null;
    }
}
