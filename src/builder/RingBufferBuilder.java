package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.object.RingBuffer;

abstract class RingBufferBuilder<T> extends AbstractRingBufferBuilder<RingBuffer<T>> {
    RingBufferBuilder(int capacity) {
        super(capacity);
    }

    abstract RingBufferBuilder<?> discarding();

    void discarding0() {
        type = RingBufferType.DISCARDING;
    }

    @SuppressWarnings("unchecked")
    public T[] getBuffer() {
        return (T[]) new Object[capacity];
    }
}
