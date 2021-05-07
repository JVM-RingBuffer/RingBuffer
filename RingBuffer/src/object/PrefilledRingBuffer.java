package org.ringbuffer.object;

/**
 * <pre>{@code
 * int key = ringBuffer.nextKey();
 * T element = ringBuffer.next(key);
 * // Populate element
 * ringBuffer.put(key);
 * }</pre>
 * <p>
 * If the ring buffer supports multiple writers, then external synchronization must be performed:
 *
 * <pre>{@code
 * synchronized (ringBuffer) {
 *     int key = ringBuffer.nextKey();
 *     T element = ringBuffer.next(key);
 *     // Populate element
 *     ringBuffer.put(key);
 * }
 * }</pre>
 */
public interface PrefilledRingBuffer<T> extends ObjectRingBuffer<T> {
    int nextKey();

    T next(int key);

    void put(int key);

    static <T> PrefilledRingBufferBuilder<T> withCapacity(int capacity) {
        return new PrefilledRingBufferBuilder<>(capacity);
    }
}
