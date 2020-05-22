package eu.menzani.ringbuffer.object;

/**
 * <pre>{@code
 * int key = ringBuffer.nextKey();
 * T element = ringBuffer.next(key);
 * // Populate element
 * ringBuffer.put(key);
 * }</pre>
 * <p>
 * From {@link #nextKey()} to {@link #put(int)} is an atomic operation.
 */
public interface PrefilledOverwritingRingBuffer<T> extends RingBuffer<T> {
    int nextKey();

    T next(int key);

    void put(int key);
}
