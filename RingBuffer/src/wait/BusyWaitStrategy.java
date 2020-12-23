package org.ringbuffer.wait;

/**
 * If a busy-wait strategy is used with a ring buffer supporting multiple readers and writers,
 * then it must not be locked upon.
 */
public interface BusyWaitStrategy {
    void reset();

    void tick();
}
