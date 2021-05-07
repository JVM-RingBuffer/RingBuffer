package org.ringbuffer.object;

import org.ringbuffer.wait.BusyWaitStrategy;

public interface LockfreeObjectRingBuffer<T> {
    int getCapacity();

    T take();

    T take(BusyWaitStrategy busyWaitStrategy);
}
