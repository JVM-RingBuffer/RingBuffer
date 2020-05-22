package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.HeapRingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.marshalling.BuilderProxy.*;

public class HeapRingBufferBuilder extends AbstractHeapRingBufferBuilder<HeapRingBuffer> {
    public HeapRingBufferBuilder(int capacity) {
        super(capacity);
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
    public HeapBlockingRingBufferBuilder blocking() {
        super.blocking0();
        return new HeapBlockingRingBufferBuilder(this);
    }

    @Override
    public HeapBlockingRingBufferBuilder blocking(BusyWaitStrategy busyWaitStrategy) {
        super.blocking0(busyWaitStrategy);
        return new HeapBlockingRingBufferBuilder(this);
    }

    @Override
    public HeapRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public HeapRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    HeapRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        if (copyClass) {
            return instantiateCopy(volatileHeapRingBuffer());
        }
        return volatileHeapRingBuffer(this);
    }
}
