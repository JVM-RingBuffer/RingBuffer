package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Number;
import eu.menzani.ringbuffer.marshalling.array.NativeByteArray;
import eu.menzani.ringbuffer.marshalling.array.SafeNativeByteArray;
import eu.menzani.ringbuffer.marshalling.array.UnsafeNativeByteArray;
import eu.menzani.ringbuffer.memory.Long;

abstract class AbstractNativeRingBufferBuilder<T> extends MarshallingRingBufferBuilder<T> {
    private final long capacity;
    // All fields are copied in <init>(AbstractNativeRingBufferBuilder<T>)

    AbstractNativeRingBufferBuilder(long capacity) {
        Assume.notLesser(capacity, 2L);
        if (!Number.isPowerOfTwo(capacity)) {
            throw new IllegalArgumentException("capacity must be a power of 2.");
        }
        this.capacity = capacity;
    }

    AbstractNativeRingBufferBuilder(AbstractNativeRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
    }

    long getCapacity() {
        return capacity;
    }

    long getCapacityMinusOne() {
        return capacity - 1L;
    }

    NativeByteArray getBuffer() {
        if (unsafe) {
            return new UnsafeNativeByteArray(capacity);
        }
        return new SafeNativeByteArray(capacity);
    }

    Long newCursor() {
        return memoryOrder.newLong();
    }
}
