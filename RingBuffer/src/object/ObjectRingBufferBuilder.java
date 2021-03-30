package org.ringbuffer.object;

import eu.menzani.struct.Arrays;
import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.wait.BusyWaitStrategy;

abstract class ObjectRingBufferBuilder<T> extends AbstractRingBufferBuilder<ObjectRingBuffer<T>> {
    private final int capacity;
    // All fields are copied in <init>(ObjectRingBufferBuilder<?>)

    ObjectRingBufferBuilder(int capacity) {
        validateCapacity(capacity);
        this.capacity = capacity;
    }

    ObjectRingBufferBuilder(ObjectRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    abstract ObjectRingBufferBuilder<?> discarding();

    void discarding0() {
        type = RingBufferType.DISCARDING;
    }

    @Override
    protected void lockfree0() {
        super.lockfree0();
        validateCapacityPowerOfTwo(capacity);
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

    T[] getBuffer() {
        return Arrays.allocateGeneric(capacity);
    }

    boolean[] getPositionNotModified() {
        boolean[] positionNotModified = new boolean[capacity];
        java.util.Arrays.fill(positionNotModified, true);
        return positionNotModified;
    }
}
