package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.java.Int;

abstract class AbstractHeapRingBufferBuilder<T> extends AbstractRingBufferBuilder<T> {
    AbstractHeapRingBufferBuilder(int capacity) {
        super(capacity);
        if (!Int.isPowerOfTwo(capacity)) {
            throw new IllegalArgumentException("capacity must be a power of 2.");
        }
    }

    AbstractHeapRingBufferBuilder(AbstractHeapRingBufferBuilder<?> builder) {
        super(builder.capacity);
        oneWriter = builder.oneWriter;
        oneReader = builder.oneReader;
        writeBusyWaitStrategy = builder.writeBusyWaitStrategy;
        readBusyWaitStrategy = builder.readBusyWaitStrategy;
        memoryOrder = builder.memoryOrder;
        copyClass = builder.copyClass;
    }

    public byte[] getBuffer() {
        return new byte[capacity];
    }
}
