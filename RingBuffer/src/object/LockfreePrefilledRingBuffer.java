package org.ringbuffer.object;

import org.ringbuffer.wait.BusyWaitStrategy;

public interface LockfreePrefilledRingBuffer<T> {
    int getCapacity();

    int nextKey();

    T next(int key);

    void put(int key);

    T take();

    T take(BusyWaitStrategy busyWaitStrategy);
}
