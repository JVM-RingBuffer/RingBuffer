package eu.menzani.ringbuffer;

import java.util.function.Consumer;

public interface RingBuffer<T> {
    int getCapacity();

    /**
     * If the ring buffer supports multiple readers and is blocking and pre-filled, then after the returned
     * object has been read, {@link #advance()} must be called.
     *
     * <pre>{@code
     * T element = ringBuffer.take();
     * // Read element
     * ringBuffer.advance();
     * }</pre>
     */
    T take();

    /**
     * If the ring buffer supports multiple readers and is blocking and pre-filled, then this method
     * must be called after the object returned by {@link #take()} has been read.
     */
    void advance();

    /**
     * If the ring buffer supports at least one reader and writer, then this method allows to take
     * elements in batches. When it returns, at least {@code size} elements are available,
     * and {@link #takePlain()} must be invoked {@code size} times to take them all.
     * <p>
     * Moreover, if the ring buffer supports multiple readers, then after the last element has been read,
     * {@link #advanceBatch()} must be called.
     *
     * <pre>{@code
     * ringBuffer.takeBatch(size);
     * for (int i = size; i > 0; i--) {
     *     T element = ringBuffer.takePlain();
     *     // Read element
     * }
     * ringBuffer.advanceBatch();
     * }</pre>
     */
    void takeBatch(int size);

    /**
     * When taking elements in batches, this method must be called {@code size} times after
     * {@link #takeBatch(int) takeBatch(size)} has returned.
     */
    T takePlain();

    /**
     * When taking elements in batches, if the ring buffer supports multiple readers,
     * then this method must be called after the last element returned by {@link #takePlain()} has been read.
     */
    void advanceBatch();

    /**
     * If the ring buffer supports one reader and multiple writers, or multiple readers and one writer,
     * and is not blocking nor discarding, then this method can only be called from the reader thread.
     */
    void forEach(Consumer<T> action);

    /**
     * If the ring buffer supports one reader and multiple writers, or multiple readers and one writer,
     * and is not blocking nor discarding, then this method can only be called from the reader thread.
     */
    boolean contains(T element);

    /**
     * If the ring buffer supports one reader and multiple writers, or multiple readers and one writer,
     * and is not blocking nor discarding, then this method can only be called from the reader thread.
     */
    int size();

    /**
     * If the ring buffer supports one reader and multiple writers, or multiple readers and one writer,
     * and is not blocking nor discarding, then this method can only be called from the reader thread.
     */
    boolean isEmpty();

    /**
     * If the ring buffer supports one reader and multiple writers, or multiple readers and one writer,
     * and is not blocking nor discarding, then this method can only be called from the reader thread.
     */
    String toString();
}
