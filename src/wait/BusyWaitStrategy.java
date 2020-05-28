package org.ringbuffer.wait;

public interface BusyWaitStrategy {
    void reset();

    void tick();
}
