package eu.menzani.ringbuffer;

public interface AbstractRingBuffer<T> {
    int getCapacity();

    T take();

    int size();

    boolean isEmpty();
}
