package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public interface RingBuffer<T> {
    int getCapacity();

    /**
     * If the ring buffer is not local, then after the returned object has been populated,
     * <code>put()</code> must be called.
     * <br>
     * Moreover, if the ring buffer supports multiple writers, then external synchronization
     * must be performed between the two method invocations:
     *
     * <pre>{@code synchronized (ringBuffer) {
     *     T element = ringBuffer.next();
     *     // Populate element
     *     ringBuffer.put();
     * } }</pre>
     */
    T next();

    void put();

    void put(T element);

    /**
     * If the ring buffer supports multiple readers and is blocking and pre-filled,
     * then external synchronization must be performed while reading elements taken out:
     *
     * <pre>{@code synchronized (ringBuffer) {
     *     T element = ringBuffer.take();
     *     // Read element
     * } }</pre>
     */
    T take();

    /**
     * If the ring buffer supports a single reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    boolean contains(T element);

    /**
     * If the ring buffer supports a single reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    int size();

    /**
     * If the ring buffer supports a single reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    boolean isEmpty();

    /**
     * If the ring buffer supports a single reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    String toString();

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
