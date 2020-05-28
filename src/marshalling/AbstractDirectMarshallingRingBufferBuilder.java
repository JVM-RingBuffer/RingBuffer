package org.ringbuffer.marshalling;

import org.ringbuffer.java.Assume;
import org.ringbuffer.java.Number;
import org.ringbuffer.marshalling.array.SafeDirectByteArray;
import org.ringbuffer.marshalling.array.UnsafeDirectByteArray;
import org.ringbuffer.memory.Long;

abstract class AbstractDirectMarshallingRingBufferBuilder<T> extends AbstractBaseMarshallingRingBufferBuilder<T> {
    private final long capacity;
    private DirectByteArray.Factory byteArrayFactory;
    // All fields are copied in <init>(AbstractDirectMarshallingRingBufferBuilder<T>)

    AbstractDirectMarshallingRingBufferBuilder(long capacity) {
        Assume.notLesser(capacity, 2L);
        if (!Number.isPowerOfTwo(capacity)) {
            throw new IllegalArgumentException("capacity must be a power of 2.");
        }
        this.capacity = capacity;
    }

    AbstractDirectMarshallingRingBufferBuilder(AbstractDirectMarshallingRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
        byteArrayFactory = builder.byteArrayFactory;
    }

    public abstract AbstractDirectMarshallingRingBufferBuilder<T> withByteArray(DirectByteArray.Factory factory);

    void withByteArray0(DirectByteArray.Factory factory) {
        byteArrayFactory = factory;
    }

    long getCapacity() {
        return capacity;
    }

    long getCapacityMinusOne() {
        return capacity - 1L;
    }

    DirectByteArray getBuffer() {
        if (byteArrayFactory != null) {
            return byteArrayFactory.newInstance(capacity);
        }
        if (unsafe) {
            return new UnsafeDirectByteArray(capacity);
        }
        return new SafeDirectByteArray(capacity);
    }

    Long newCursor() {
        return memoryOrder.newLong();
    }
}
