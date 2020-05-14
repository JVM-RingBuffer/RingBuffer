package eu.menzani.ringbuffer;

import java.util.function.Supplier;

/**
 * <pre>{@code
 * int key = ringBuffer.nextKey();
 * int putKey = ringBuffer.nextPutKey(key);
 * T element = ringBuffer.next(key, putKey);
 * // Populate element
 * ringBuffer.put(putKey);
 * }</pre>
 * <p>
 * From {@link #nextKey()} to {@link #put(int)} is an atomic operation.
 */
public interface PrefilledRingBuffer<T> extends RingBuffer<T> {
    int nextKey();

    int nextPutKey(int key);

    T next(int key, int putKey);

    void put(int putKey);

    static <T> OverwritingPrefilledRingBufferBuilder<T> withCapacityAndFiller(int capacity, Supplier<? extends T> filler) {
        return new OverwritingPrefilledRingBufferBuilder<>(capacity, filler);
    }
}
