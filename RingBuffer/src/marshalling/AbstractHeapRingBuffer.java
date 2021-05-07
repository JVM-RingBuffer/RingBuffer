package org.ringbuffer.marshalling;

import eu.menzani.struct.AbstractHeapBuffer;
import org.ringbuffer.AbstractRingBuffer;

interface AbstractHeapRingBuffer extends AbstractHeapBuffer, AbstractRingBuffer {
    void put(int offset);

    /**
     * If the ring buffer supports multiple readers, then external synchronization must be performed:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *     int offset = ringBuffer.take(...);
     *     // Read data
     *     ringBuffer.advance(...); // If needed
     * }
     * }</pre>
     */
    int take(int size);

    int size();
}
