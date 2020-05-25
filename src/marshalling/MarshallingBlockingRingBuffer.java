package eu.menzani.ringbuffer.marshalling;

public interface MarshallingBlockingRingBuffer extends AbstractMarshallingRingBuffer {
    int next(int size);

    void advance(int offset);
}
