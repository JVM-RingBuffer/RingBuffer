package eu.menzani.ringbuffer.marshalling;

public interface HeapBlockingRingBuffer extends AbstractHeapRingBuffer {
    int next(int size);

    void advance(int offset);
}
