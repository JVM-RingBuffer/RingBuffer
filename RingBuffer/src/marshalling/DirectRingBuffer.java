package org.ringbuffer.marshalling;

public interface DirectRingBuffer extends AbstractDirectRingBuffer {
    /**
     * If the ring buffer supports multiple writers and is not lock-free, then external synchronization must be performed:
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

    /**
     * If the ring buffer is lock-free, then this method must not be called.
     */
    void advance(long offset);

    static DirectClearingRingBufferBuilder withCapacity(long capacity) {
        return new DirectClearingRingBufferBuilder(capacity);
    }
}
