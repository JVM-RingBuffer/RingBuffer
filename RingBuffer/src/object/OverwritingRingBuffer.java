package org.ringbuffer.object;

public interface OverwritingRingBuffer<T> extends OverwritingObjectRingBuffer<T> {
    void put(T element);
}
