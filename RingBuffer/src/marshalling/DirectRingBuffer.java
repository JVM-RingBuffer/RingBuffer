package org.ringbuffer.marshalling;

public interface DirectRingBuffer extends AbstractDirectRingBuffer {
    /**
     * If the ring buffer supports multiple writers, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *     long offset = ringBuffer.next(...);
     *     // Write data
     *     ringBuffer.put(...);
     * }
     * }</pre>
     */
    long next(long size);

    void advance(long offset);

    static DirectClearingRingBufferBuilder withCapacity(long capacity) {
        return new DirectClearingRingBufferBuilder(capacity);
    }
}
