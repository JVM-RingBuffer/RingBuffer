package eu.menzani.ringbuffer.marshalling;

/**
 * From {@link #next(long)} to {@link #put(long)} is an atomic operation.
 * From {@link #take(long)} to {@link #advance(long)} is an atomic operation.
 */
public interface DirectMarshallingBlockingRingBuffer extends AbstractDirectMarshallingRingBuffer {
    long next(long size);

    void advance(long offset);
}
