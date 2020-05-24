package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Number;
import eu.menzani.ringbuffer.marshalling.array.ByteArray;
import eu.menzani.ringbuffer.marshalling.array.SafeByteArray;
import eu.menzani.ringbuffer.marshalling.array.UnsafeByteArray;
import eu.menzani.ringbuffer.memory.Integer;

abstract class AbstractHeapRingBufferBuilder<T> extends MarshallingRingBufferBuilder<T> {
    private final int capacity;
    // All fields are copied in <init>(AbstractHeapRingBufferBuilder<T>)

    AbstractHeapRingBufferBuilder(int capacity) {
        Assume.notLesser(capacity, 2);
        if (!Number.isPowerOfTwo(capacity)) {
            throw new IllegalArgumentException("capacity must be a power of 2.");
        }
        this.capacity = capacity;
    }

    AbstractHeapRingBufferBuilder(AbstractHeapRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    int getCapacity() {
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    ByteArray getBuffer() {
        if (unsafe) {
            return new UnsafeByteArray(capacity);
        }
        return new SafeByteArray(capacity);
    }

    Integer newCursor() {
        return memoryOrder.newInteger();
    }
}
