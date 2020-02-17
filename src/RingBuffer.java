package eu.menzani.ringbuffer;

public interface RingBuffer<T> extends AbstractRingBuffer<T> {
    void put(T element);
}
