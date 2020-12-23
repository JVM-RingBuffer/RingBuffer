package org.ringbuffer.object;

/**
 * <pre>{@code
 * int key = ringBuffer.nextKey();
 * int putKey = ringBuffer.nextPutKey(key);
 * T element = ringBuffer.next(key, putKey);
 * // Populate element
 * ringBuffer.put(putKey);
 * }</pre>
 * <p>
 * If the ring buffer supports multiple writers, then external synchronization must be performed:
 *
 * <pre>{@code
 * synchronized (ringBuffer) {
 *     int key = ringBuffer.nextKey();
 *     int putKey = ringBuffer.nextPutKey(key);
 *     T element = ringBuffer.next(key, putKey);
 *     // Populate element
 *     ringBuffer.put(putKey);
 * }
 * }</pre>
 */
public interface PrefilledRingBuffer2<T> extends ObjectRingBuffer<T> {
    int nextKey();

    int nextPutKey(int key);

    T next(int key, int putKey);

    void put(int putKey);
}
