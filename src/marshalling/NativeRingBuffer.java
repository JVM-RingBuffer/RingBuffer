package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.builder.NativeRingBufferBuilder;

public interface NativeRingBuffer extends AbstractNativeRingBuffer {
    long next();

    void advance();

    static NativeRingBufferBuilder withCapacity(long capacity) {
        return new NativeRingBufferBuilder(capacity);
    }
}
