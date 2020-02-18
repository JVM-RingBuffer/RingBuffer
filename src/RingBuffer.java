package eu.menzani.ringbuffer;

public interface RingBuffer<T> {
    int getCapacity();

    T put();

    void commit();

    void put(T element);

    T take();

    int size();

    boolean isEmpty();
}
