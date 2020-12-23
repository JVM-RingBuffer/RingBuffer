package org.ringbuffer.marshalling;

import eu.menzani.atomic.DirectAtomicBooleanArray;
import eu.menzani.buffer.DirectBuffer;
import eu.menzani.system.Garbage;
import org.ringbuffer.AbstractRingBufferBuilder;

abstract class AbstractDirectRingBufferBuilder<T> extends MarshallingRingBufferBuilder<T> {
    private final long capacity;
    // All fields are copied in <init>(AbstractDirectRingBufferBuilder<?>)

    private transient final long[] memoryToFree = new long[2];

    AbstractDirectRingBufferBuilder(long capacity) {
        validateCapacity(capacity);
        validateCapacityPowerOfTwo(capacity);
        this.capacity = capacity;
    }

    AbstractDirectRingBufferBuilder(AbstractDirectRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    @Override
    protected abstract AbstractRingBufferBuilder<?> withoutLocks();

    @Override
    protected void withoutLocks0() {
        super.withoutLocks0();
        validateCapacityPowerOfTwo(capacity);
    }

    long getCapacity() {
        return capacity;
    }

    long getCapacityMinusOne() {
        return capacity - 1L;
    }

    long getBuffer() {
        long address = DirectBuffer.allocate(capacity);
        memoryToFree[0] = address;
        return address;
    }

    long getPositionNotModified() {
        long address = DirectAtomicBooleanArray.allocate(capacity, true);
        memoryToFree[1] = address;
        return address;
    }

    @Override
    protected void afterBuild(T ringBuffer) {
        Garbage.freeMemory(ringBuffer, memoryToFree);
    }
}
