package eu.menzani.ringbuffer.marshalling;

public interface HeapBlockingRingBuffer extends AbstractHeapRingBuffer {
    int next(int size);
}
