package org.ringbuffer.object;

/**
 * The {@code null} element may be used if the {@link #contains(Object) contains(T)} and {@link #toString()}
 * methods are never called.
 */
public interface RingBuffer<T> extends ObjectRingBuffer<T> {
    void put(T element);

    static <T> RingBufferBuilder<T> withCapacity(int capacity) {
        return new RingBufferBuilder<>(capacity);
    }
}
