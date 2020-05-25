package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.builder.DirectMarshallingRingBufferBuilder;

public interface DirectMarshallingRingBuffer extends AbstractDirectMarshallingRingBuffer {
    long next();

    void advance();

    static DirectMarshallingRingBufferBuilder withCapacity(long capacity) {
        return new DirectMarshallingRingBufferBuilder(capacity);
    }
}
