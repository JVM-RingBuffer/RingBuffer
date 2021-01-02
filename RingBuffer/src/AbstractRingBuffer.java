package org.ringbuffer;

public interface AbstractRingBuffer {
    Object getReadMonitor();

    /**
     * If the ring buffer supports one reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    boolean isEmpty();

    /**
     * If the ring buffer supports one reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    boolean isNotEmpty();
}
