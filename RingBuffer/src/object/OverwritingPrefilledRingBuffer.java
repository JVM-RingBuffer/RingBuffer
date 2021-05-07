package org.ringbuffer.object;

public interface OverwritingPrefilledRingBuffer<T> extends OverwritingObjectRingBuffer<T> {
    T next();
}
