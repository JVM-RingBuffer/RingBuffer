package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public interface RingBuffer<T> {
    int getCapacity();

    /**
     * If the ring buffer is non-local, then after the returned object has been populated,
     * <code>put()</code> must be called.
     */
    T next();

    void put();

    void put(T element);

    T take();

    boolean contains(T element);

    int size();

    boolean isEmpty();

    static <T> RingBufferBuilder<T> empty(int capacity) {
        return new RingBufferBuilder<>(capacity, null, null);
    }

    static <T> RingBufferBuilder<T> empty(int capacity, T dummyElement) {
        return new RingBufferBuilder<>(capacity, null, dummyElement);
    }

    static <T> RingBufferBuilder<T> prefilled(int capacity, Supplier<? extends T> filler) {
        return new RingBufferBuilder<>(capacity, filler, null);
    }
}
