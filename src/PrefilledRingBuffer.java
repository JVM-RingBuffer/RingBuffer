package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public interface PrefilledRingBuffer<T> extends RingBuffer<T> {
    /**
     * If the ring buffer supports at least one reader and writer, then after the returned object has been
     * populated, {@link #put()} must be called.
     *
     * <pre>{@code
     * T element = ringBuffer.next();
     * // Populate element
     * ringBuffer.put();
     * }</pre>
     */
    T next();

    /**
     * If the ring buffer supports at least one reader and writer, then this method must be called after
     * the object returned by {@link #next()} has been populated.
     */
    void put();

    static <T> PrefilledRingBufferBuilder<T> withCapacityAndFiller(int capacity, Supplier<? extends T> filler) {
        return new PrefilledRingBufferBuilder<>(capacity, filler);
    }
}
