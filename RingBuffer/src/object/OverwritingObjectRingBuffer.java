package org.ringbuffer.object;

import eu.menzani.concurrent.ThreadLocal;
import org.ringbuffer.wait.BusyWaitStrategy;

public interface OverwritingObjectRingBuffer<T> {
    int getCapacity();

    T take();

    T take(@ThreadLocal BusyWaitStrategy busyWaitStrategy);

    int size();

    boolean isEmpty();

    boolean isNotEmpty();
}
