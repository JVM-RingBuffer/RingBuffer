package eu.menzani.ringbuffer;

public interface PrefilledRingBuffer<T> {
    int getCapacity();

    T put();

    void endPut();

    T take();

    int size();

    boolean isEmpty();

    boolean isNotEmpty();
}
