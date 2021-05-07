package org.ringbuffer.marshalling;

import eu.menzani.struct.AbstractHeapBuffer;
import org.ringbuffer.wait.BusyWaitStrategy;

public interface LockfreeHeapRingBuffer extends AbstractHeapBuffer {
    int next(int size);

    /**
     * {@code offset} is the value returned by {@link LockfreeHeapRingBuffer#next(int)}.
     */
    void put(int offset);

    int take(int size);

    int take(int size, BusyWaitStrategy busyWaitStrategy);
}
