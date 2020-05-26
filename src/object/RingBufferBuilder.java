package eu.menzani.ringbuffer.object;

import eu.menzani.ringbuffer.AbstractRingBufferBuilder;
import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.lang.invoke.MethodHandles;

abstract class RingBufferBuilder<T> extends AbstractRingBufferBuilder<RingBuffer<T>> {
    private static final MethodHandles.Lookup implLookup = MethodHandles.lookup();

    @Override
    protected MethodHandles.Lookup getImplLookup() {
        return implLookup;
    }

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

    @Override
    protected BusyWaitStrategy getWriteBusyWaitStrategy() {
        return super.getWriteBusyWaitStrategy();
    }

    @Override
    protected BusyWaitStrategy getReadBusyWaitStrategy() {
        return super.getReadBusyWaitStrategy();
    }

    int getCapacity() {
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    @SuppressWarnings("unchecked")
    T[] getBuffer() {
        return (T[]) new Object[capacity];
    }

    Integer newCursor() {
        return memoryOrder.newInteger();
    }
}
