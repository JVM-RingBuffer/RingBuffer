package eu.menzani.ringbuffer.marshalling;

/**
 * From {@link #next()} to {@link #put(int)} is an atomic operation.
 * From {@link #take(int)} to {@link #advance()} is an atomic operation.
 */
public interface MarshallingRingBuffer extends AbstractMarshallingRingBuffer {
    int next();

    void advance();

    static MarshallingRingBufferBuilder withCapacity(int capacity) {
        return new MarshallingRingBufferBuilder(capacity);
    }
}
