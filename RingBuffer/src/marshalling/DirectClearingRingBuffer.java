package org.ringbuffer.marshalling;

public interface DirectClearingRingBuffer extends AbstractDirectRingBuffer {
    /**
     * If the ring buffer supports multiple writers, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *     long offset = ringBuffer.next();
     *     // Write data
     *     ringBuffer.put(...);
     * }
     * }</pre>
     */
    long next();
}
