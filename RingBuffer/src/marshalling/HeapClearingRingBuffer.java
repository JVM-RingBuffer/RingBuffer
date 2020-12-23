package org.ringbuffer.marshalling;

public interface HeapClearingRingBuffer extends AbstractHeapRingBuffer {
    /**
     * If the ring buffer supports multiple writers, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *     int offset = ringBuffer.next();
     *     // Write data
     *     ringBuffer.put(...);
     * }
     * }</pre>
     */
    int next();
}
