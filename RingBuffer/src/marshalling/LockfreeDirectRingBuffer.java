package org.ringbuffer.marshalling;

import eu.menzani.struct.AbstractDirectBuffer;
import org.ringbuffer.wait.BusyWaitStrategy;

public interface LockfreeDirectRingBuffer extends AbstractDirectBuffer {
    long next(long size);

    /**
     * {@code offset} is the value returned by {@link LockfreeDirectRingBuffer#next(long)}.
     */
    void put(long offset);

    long take(long size);

    long take(long size, BusyWaitStrategy busyWaitStrategy);
}
