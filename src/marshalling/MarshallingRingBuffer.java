package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.builder.MarshallingRingBufferBuilder;

public interface MarshallingRingBuffer extends AbstractMarshallingRingBuffer {
    int next();

    void advance();

    static MarshallingRingBufferBuilder withCapacity(int capacity) {
        return new MarshallingRingBufferBuilder(capacity);
    }
}
