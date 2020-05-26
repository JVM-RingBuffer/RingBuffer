package eu.menzani.ringbuffer.marshalling;

public interface MarshallingRingBuffer extends AbstractMarshallingRingBuffer {
    int next();

    void advance();

    static MarshallingRingBufferBuilder withCapacity(int capacity) {
        return new MarshallingRingBufferBuilder(capacity);
    }
}
