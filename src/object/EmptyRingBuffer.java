package eu.menzani.ringbuffer.object;

public interface EmptyRingBuffer<T> extends RingBuffer<T> {
    void put(T element);

    static <T> EmptyRingBufferBuilder<T> withCapacity(int capacity) {
        return new EmptyRingBufferBuilder<>(capacity);
    }
}
