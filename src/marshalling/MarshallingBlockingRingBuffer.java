package org.ringbuffer.marshalling;

/**
 * From {@link #next(int)} to {@link #put(int)} is an atomic operation.
 * From {@link #take(int)} to {@link #advance(int)} is an atomic operation.
 */
public interface MarshallingBlockingRingBuffer extends AbstractMarshallingRingBuffer {
    int next(int size);

    void advance(int offset);
}
