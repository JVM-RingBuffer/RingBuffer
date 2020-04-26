package eu.menzani.ringbuffer.wait;

import eu.menzani.ringbuffer.RingBufferBuilder;

/**
 * If synchronization is performed on this instance, then it cannot be used with
 * {@link RingBufferBuilder#waitingWith(BusyWaitStrategy)}
 * if the ring buffer supports multiple readers and writers.
 */
public interface BusyWaitStrategy {
    void reset();

    void tick();
}
