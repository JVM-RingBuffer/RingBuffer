package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The <code>null</code> element may be used if the {@link #contains(T)} and {@link #toString()} methods are never called.
 */
public interface RingBuffer<T> {
    int getCapacity();

    /**
     * If the ring buffer supports at least one reader and writer, then after the returned object has been populated,
     * {@link #put()} must be called.
     * <p>
     * Moreover, if the ring buffer supports multiple writers and is discarding, then external
     * synchronization must be performed between the two method invocations:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *    T element = ringBuffer.next();
     *    // Populate element
     *    ringBuffer.put();
     * }
     * }</pre>
     * <p>
     * If the ring buffer is not discarding, then {@link #next(int)} must be used instead.
     */
    T next();

    void put();

    default int nextKey() {
        return 0;
    }

    /**
     * To be used if the ring buffer supports multiple writers and is not discarding.
     *
     * <pre>{@code
     * int key = ringBuffer.nextKey();
     * T element = ringBuffer.next(key);
     * // Populate element
     * ringBuffer.put(key);
     * }</pre>
     */
    default T next(int key) {
        return next();
    }

    default void put(int key) {
        put();
    }

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
     * <p>
     * If the ring buffer supports multiple writers and is blocking and pre-filled, then after the element
     * has been read, {@link #advance()} must be called.
     */
    T take();

    /**
     * If the ring buffer is blocking and pre-filled, then after the buffer has been read,
     * {@link #advanceBatch()} must be called.
     * <p>
     * Moreover, if the ring buffer supports multiple readers, then external synchronization
     * must be performed while reading elements taken out:
     *
     * <pre>{@code
     * synchronized (ringBuffer) {
     *    ringBuffer.fill(buffer);
     *    // Read buffer
     *    ringBuffer.advanceBatch();
     * }
     * }</pre>
     * <p>
     * If external synchronization is not performed, then the buffer cannot be shared by multiple threads.
     */
    void fill(Array<T> buffer);

    default void advance() {}

    default void advanceBatch() {
        advance();
    }

    /**
     * If the ring buffer supports a single reader and is not blocking nor discarding,
     * or the ring buffer supports multiple writers and is blocking,
     * then this method can only be called from the reader thread.
     */
    void forEach(Consumer<T> action);

    /**
     * If the ring buffer supports a single reader and is not blocking nor discarding,
     * or the ring buffer supports multiple writers and is blocking,
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
     * or the ring buffer supports multiple writers and is blocking,
     * then this method can only be called from the reader thread.
     * <p>
     * If the ring buffer supports multiple writers and is blocking, then the element display order is undefined,
     * otherwise it is equal to the read order.
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
