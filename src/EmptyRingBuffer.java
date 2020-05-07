package eu.menzani.ringbuffer;

/**
 * The {@code null} element may be used if the {@link #contains(Object) contains(T)} and {@link #toString()}
 * methods are never called.
 */
public interface EmptyRingBuffer<T> extends RingBuffer<T> {
    void put(T element);

    static <T> EmptyRingBufferBuilder<T> withCapacity(int capacity) {
        return new EmptyRingBufferBuilder<>(capacity);
    }
}
