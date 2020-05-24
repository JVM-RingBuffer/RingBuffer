package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.HeapBlockingRingBuffer;
import eu.menzani.ringbuffer.memory.MemoryOrder;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import static eu.menzani.ringbuffer.marshalling.BuilderProxy.*;

public class HeapBlockingRingBufferBuilder extends AbstractHeapRingBufferBuilder<HeapBlockingRingBuffer> {
    HeapBlockingRingBufferBuilder(HeapRingBufferBuilder builder) {
        super(builder);
    }

    @Override
    public HeapBlockingRingBufferBuilder oneWriter() {
        super.oneWriter0();
        return this;
    }

    @Override
    public HeapBlockingRingBufferBuilder manyWriters() {
        super.manyWriters0();
        return this;
    }

    @Override
    public HeapBlockingRingBufferBuilder oneReader() {
        super.oneReader0();
        return this;
    }

    @Override
    public HeapBlockingRingBufferBuilder manyReaders() {
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
    public HeapBlockingRingBufferBuilder waitingWith(BusyWaitStrategy busyWaitStrategy) {
        super.waitingWith0(busyWaitStrategy);
        return this;
    }

    @Override
    public HeapBlockingRingBufferBuilder withMemoryOrder(MemoryOrder memoryOrder) {
        super.withMemoryOrder0(memoryOrder);
        return this;
    }

    @Override
    public HeapBlockingRingBufferBuilder copyClass() {
        super.copyClass0();
        return this;
    }

    @Override
    public HeapBlockingRingBufferBuilder unsafe() {
        super.unsafe0();
        return this;
    }

    @Override
    HeapBlockingRingBuffer create(RingBufferConcurrency concurrency, RingBufferType type) {
        switch (concurrency) {
            case VOLATILE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(volatileHeapBlockingRingBuffer());
                    }
                    return volatileHeapBlockingRingBuffer(this);
                }
            case ATOMIC_READ:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicReadHeapBlockingRingBuffer());
                    }
                    return atomicReadHeapBlockingRingBuffer(this);
                }
            case ATOMIC_WRITE:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(atomicWriteHeapBlockingRingBuffer());
                    }
                    return atomicWriteHeapBlockingRingBuffer(this);
                }
            case CONCURRENT:
                if (type == RingBufferType.OVERWRITING) {
                    if (copyClass) {
                        return instantiateCopy(concurrentHeapBlockingRingBuffer());
                    }
                    return concurrentHeapBlockingRingBuffer(this);
                }
        }
        throw new AssertionError();
    }
}
