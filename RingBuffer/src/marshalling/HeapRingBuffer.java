package org.ringbuffer.marshalling;

public interface HeapRingBuffer extends AbstractHeapRingBuffer {
    /**
     * If the ring buffer supports multiple writers and is not lock-free, then external synchronization must be performed:
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

    /**
     * If the ring buffer is lock-free, then this method must not be called.
     */
    void advance(int offset);

    static HeapClearingRingBufferBuilder withCapacity(int capacity) {
        return new HeapClearingRingBufferBuilder(capacity);
    }
}
