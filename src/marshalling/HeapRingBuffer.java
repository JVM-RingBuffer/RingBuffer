package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.builder.HeapRingBufferBuilder;

public interface HeapRingBuffer extends AbstractHeapRingBuffer {
    int next();

    void advance();

    static HeapRingBufferBuilder withCapacity(int capacity) {
        return new HeapRingBufferBuilder(capacity);
    }
}
