package eu.menzani.ringbuffer;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The {@code null} element may be used if the {@link #contains(Object) contains(T)} and {@link #toString()}
 * methods are never called.
 */
public interface RingBuffer<T> {
    int getCapacity();

    /**
     * Returns the monitor to be used when synchronizing externally while reading.
     */
    Object getReadMonitor();

    /**
     * If the ring buffer supports at least one reader and writer, then after the returned object has been
     * populated, {@link #put()} must be called.
     * <p>
     * Moreover, if the ring buffer supports multiple writers, then external synchronization must be performed
     * between the two method invocations:
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

    /**
     * If the ring buffer supports at least one reader and writer, then this method must be called after
     * the object returned by {@link #next()} has been populated.
     */
    void put();

    void put(T element);

    /**
     * If the ring buffer supports multiple readers and is blocking and pre-filled, then external synchronization
     * must be performed while reading elements taken out:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *    T element = ringBuffer.take();
     *    // Read element
     * }
     * }</pre>
     *
     * If the ring buffer supports one writer, then synchronization can be performed on this instance.
     */
    T take();

    /**
     * If the ring buffer supports at least one reader and writer, then this method allows to take
     * elements in batches. When it returns, at least {@code size} elements are available,
     * and {@link #takePlain()} must be invoked {@code size} times to take them all.
     * <p>
     * Moreover, if the ring buffer supports multiple readers, then external synchronization must be
     * performed during the whole process:
     *
     * <pre>{@code
     * synchronized (ringBuffer.getReadMonitor()) {
     *    ringBuffer.takeBatch(size);
     *    for (int i = size; i > 0; i--) {
     *        T element = ringBuffer.takePlain();
     *        // Read element
     *    }
     * }
     * }</pre>
     *
     * If the ring buffer supports one writer, then synchronization can be performed on this instance.
     */
    void takeBatch(int size);

    /**
     * Does not possibly wait until at least one element is available.
     * <p>
     * If the ring buffer supports at least one reader and writer, then this method must be repeatedly invoked
     * after {@link #takeBatch(int)} has returned.
     * Otherwise, {@link #take()} will be equal to this method.
     */
    T takePlain();

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
