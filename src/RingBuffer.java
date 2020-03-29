package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface RingBuffer<T> {
    int getCapacity();

    /**
     * If the ring buffer is not local, then after the returned object has been populated,
     * <code>put()</code> must be called.
     * <p>
     * Moreover, if the ring buffer supports multiple writers, then external synchronization
     * must be performed between the two method invocations:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *    T element = ringBuffer.next();
     *    // Populate element
     *    ringBuffer.put();
     * }
     * }</pre>
     */
    T next();

    default void put() {}

    void put(T element);

    /**
     * If the ring buffer supports multiple readers and is blocking and pre-filled,
     * then external synchronization must be performed while reading elements taken out:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *    T element = ringBuffer.take();
     *    // Read element
     * }
     * }</pre>
     */
    T take();

    /**
     * If the ring buffer is blocking and pre-filled, then after the buffer has been read,
     * <code>advance()</code> must be called.
     * <p>
     * Moreover, if the ring buffer supports multiple readers, then external synchronization
     * must be performed while reading elements taken out:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *    ringBuffer.fill(buffer);
     *    // Read buffer
     *    ringBuffer.advance();
     * }
     * }</pre>
     * <p>
     * If external synchronization is not performed, then the buffer cannot be shared by multiple threads.
     */
    void fill(Array<T> buffer);

    default void advance() {}

    /**
     * If the ring buffer supports a single reader and is not blocking nor discarding,
     * then this method can only be called from the reader thread.
     */
    void forEach(Consumer<T> action);

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
