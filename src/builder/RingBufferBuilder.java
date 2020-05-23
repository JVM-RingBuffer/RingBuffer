package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.object.RingBuffer;

abstract class RingBufferBuilder<T> extends AbstractRingBufferBuilder<RingBuffer<T>> {
    final int capacity;
    // All fields are copied in <init>(RingBufferBuilder<T>)

    RingBufferBuilder(int capacity) {
        Assume.notLesser(capacity, 2);
        this.capacity = capacity;
    }

    RingBufferBuilder(RingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    abstract RingBufferBuilder<?> discarding();

    void discarding0() {
        type = RingBufferType.DISCARDING;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCapacityMinusOne() {
        return capacity - 1;
    }

    @SuppressWarnings("unchecked")
    public T[] getBuffer() {
        return (T[]) new Object[capacity];
    }

    public Integer newCursor() {
        return memoryOrder.newInteger();
    }
}
