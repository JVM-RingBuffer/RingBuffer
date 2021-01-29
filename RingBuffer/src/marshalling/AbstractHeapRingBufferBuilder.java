package org.ringbuffer.marshalling;

import eu.menzani.struct.HeapBuffer;

import java.util.Arrays;

abstract class AbstractHeapRingBufferBuilder<T> extends MarshallingRingBufferBuilder<T> {
    private final int capacity;
    // All fields are copied in <init>(AbstractHeapRingBufferBuilder<?>)

    AbstractHeapRingBufferBuilder(int capacity) {
        validateCapacity(capacity);
        validateCapacityPowerOfTwo(capacity);
        this.capacity = capacity;
    }

    AbstractHeapRingBufferBuilder(AbstractHeapRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    @Override
    protected void lockfree0() {
        super.lockfree0();
        validateCapacityPowerOfTwo(capacity);
    }

    int getCapacity() {
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    byte[] getBuffer() {
        return HeapBuffer.allocate(capacity);
    }

    boolean[] getPositionNotModified() {
        boolean[] positionNotModified = new boolean[capacity];
        Arrays.fill(positionNotModified, true);
        return positionNotModified;
    }
}
