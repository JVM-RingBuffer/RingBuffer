package org.ringbuffer.marshalling;

public interface HeapRingBuffer extends AbstractHeapRingBuffer {
    /**
     * If the ring buffer supports multiple writers, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *     int offset = ringBuffer.next(...);
     *     // Write data
     *     ringBuffer.put(...);
     * }
     * }</pre>
     */
    int next(int size);

    void advance(int offset);

    static HeapClearingRingBufferBuilder withCapacity(int capacity) {
        return new HeapClearingRingBufferBuilder(capacity);
    }
}
