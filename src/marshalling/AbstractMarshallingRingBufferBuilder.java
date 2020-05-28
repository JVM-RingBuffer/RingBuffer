package org.ringbuffer.marshalling;

import org.ringbuffer.java.Assume;
import org.ringbuffer.java.Number;
import org.ringbuffer.marshalling.array.SafeByteArray;
import org.ringbuffer.marshalling.array.UnsafeByteArray;
import org.ringbuffer.memory.Integer;

abstract class AbstractMarshallingRingBufferBuilder<T> extends AbstractBaseMarshallingRingBufferBuilder<T> {
    private final int capacity;
    private ByteArray.Factory byteArrayFactory;
    // All fields are copied in <init>(AbstractMarshallingRingBufferBuilder<T>)

    AbstractMarshallingRingBufferBuilder(int capacity) {
        Assume.notLesser(capacity, 2);
        if (!Number.isPowerOfTwo(capacity)) {
            throw new IllegalArgumentException("capacity must be a power of 2.");
        }
        this.capacity = capacity;
    }

    AbstractMarshallingRingBufferBuilder(AbstractMarshallingRingBufferBuilder<?> builder) {
        super(builder);
        capacity = builder.capacity;
        byteArrayFactory = builder.byteArrayFactory;
    }

    public abstract AbstractMarshallingRingBufferBuilder<T> withByteArray(ByteArray.Factory factory);

    void withByteArray0(ByteArray.Factory factory) {
        byteArrayFactory = factory;
    }

    int getCapacity() {
        return capacity;
    }

    int getCapacityMinusOne() {
        return capacity - 1;
    }

    ByteArray getBuffer() {
        if (byteArrayFactory != null) {
            return byteArrayFactory.newInstance(capacity);
        }
        if (unsafe) {
            return new UnsafeByteArray(capacity);
        }
        return new SafeByteArray(capacity);
    }

    Integer newCursor() {
        return memoryOrder.newInteger();
    }
}
