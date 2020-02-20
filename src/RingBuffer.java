package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public interface RingBuffer<T> {
    int getCapacity();

    T put();

    void commit();

    void put(T element);

    T take();

    boolean contains(T element);

    int size();

    boolean isEmpty();

    static RingBufferBuilder empty(int capacity) {
        return new RingBufferBuilder(capacity, null, null);
    }

    static RingBufferBuilder empty(int capacity, Object dummyElement) {
        return new RingBufferBuilder(capacity, null, dummyElement);
    }

    static RingBufferBuilder prefilled(int capacity, Supplier<?> filler) {
        return new RingBufferBuilder(capacity, filler, null);
    }
}
