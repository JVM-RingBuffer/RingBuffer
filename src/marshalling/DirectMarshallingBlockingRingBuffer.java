package eu.menzani.ringbuffer.marshalling;

public interface DirectMarshallingBlockingRingBuffer extends AbstractDirectMarshallingRingBuffer {
    long next(long size);

    void advance(long offset);
}
