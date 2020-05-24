package eu.menzani.ringbuffer.marshalling;

public interface NativeBlockingRingBuffer extends AbstractNativeRingBuffer {
    long next(long size);

    void advance(long offset);
}
