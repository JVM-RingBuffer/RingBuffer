package org.ringbuffer.marshalling;

/**
 * From {@link #next()} to {@link #put(long)} is an atomic operation.
 * From {@link #take(long)} to {@link #advance()} is an atomic operation.
 */
public interface DirectMarshallingRingBuffer extends AbstractDirectMarshallingRingBuffer {
    long next();

    void advance();

    static DirectMarshallingRingBufferBuilder withCapacity(long capacity) {
        return new DirectMarshallingRingBufferBuilder(capacity);
    }
}
