package org.ringbuffer.marshalling;

import eu.menzani.struct.AbstractDirectBuffer;
import org.ringbuffer.AbstractRingBuffer;

interface AbstractDirectRingBuffer extends AbstractDirectBuffer, AbstractRingBuffer {
    void put(long offset);

    /**
     * If the ring buffer supports multiple readers, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *     long offset = ringBuffer.take(...);
     *     // Read data
     *     ringBuffer.advance(...); // If needed
     * }
     * }</pre>
     */
    long take(long size);

    long size();
}
