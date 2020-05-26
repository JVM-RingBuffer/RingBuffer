package eu.menzani.ringbuffer.marshalling;

public interface DirectMarshallingRingBuffer extends AbstractDirectMarshallingRingBuffer {
    long next();

    void advance();

    static DirectMarshallingRingBufferBuilder withCapacity(long capacity) {
        return new DirectMarshallingRingBufferBuilder(capacity);
    }
}
