package eu.menzani.ringbuffer;

public interface RingBuffer<T> {
    int getCapacity();

    T put();

    void commit();

    void put(T element);

    T take();

    boolean contains(T element);

    int size();

    boolean isEmpty();
}
