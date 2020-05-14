package eu.menzani.ringbuffer;

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
public interface OverwritingPrefilledRingBuffer<T> extends RingBuffer<T> {
    int nextKey();

    T next(int key);

    void put(int key);
}
