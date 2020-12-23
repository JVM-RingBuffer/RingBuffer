package org.ringbuffer.object;

/**
 * <pre>{@code
 * synchronized (ringBuffer) {
 *     T element = ringBuffer.next();
 *     // Populate element
 * }
 * }</pre>
 */
public interface PrefilledOverwritingRingBuffer<T> extends ObjectRingBuffer<T> {
    T next();
}
