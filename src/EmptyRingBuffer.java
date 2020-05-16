package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.builder.EmptyRingBufferBuilder;

public interface EmptyRingBuffer<T> extends RingBuffer<T> {
    void put(T element);

    static <T> EmptyRingBufferBuilder<T> withCapacity(int capacity) {
        return new EmptyRingBufferBuilder<>(capacity);
    }
}
