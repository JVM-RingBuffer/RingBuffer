package org.ringbuffer.object;

public interface LockfreeRingBuffer<T> extends LockfreeObjectRingBuffer<T> {
    void put(T element);
}
