package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.java.Assume;
import eu.menzani.ringbuffer.java.Number;
import eu.menzani.ringbuffer.marshalling.ReportingAssertingSafeNativeByteArray;
import eu.menzani.ringbuffer.marshalling.AssertingSafeNativeByteArray;
import eu.menzani.ringbuffer.marshalling.NativeByteArray;
import eu.menzani.ringbuffer.marshalling.ReportingSafeNativeByteArray;
import eu.menzani.ringbuffer.marshalling.SafeNativeByteArray;
import eu.menzani.ringbuffer.marshalling.UnsafeNativeByteArray;
import eu.menzani.ringbuffer.memory.Long;

abstract class AbstractNativeRingBufferBuilder<T> extends AbstractRingBufferBuilder<T> {
    private final long capacity;
    private boolean unsafe;
    private boolean asserting;
    private boolean notReporting;
    // All fields are copied in <init>(AbstractNativeRingBufferBuilder<T>)

    AbstractNativeRingBufferBuilder(long capacity) {
        Assume.notLesser(capacity, 2L);
        if (!Number.isPowerOfTwo(capacity)) {
            throw new IllegalArgumentException("capacity must be a power of 2.");
        }
        this.capacity = capacity;
    }

    AbstractNativeRingBufferBuilder(AbstractNativeRingBufferBuilder<?> builder) {
        capacity = builder.capacity;
        oneWriter = builder.oneWriter;
        oneReader = builder.oneReader;
        writeBusyWaitStrategy = builder.writeBusyWaitStrategy;
        readBusyWaitStrategy = builder.readBusyWaitStrategy;
        memoryOrder = builder.memoryOrder;
        copyClass = builder.copyClass;
        unsafe = builder.unsafe;
        asserting = builder.asserting;
        notReporting = builder.notReporting;
    }

    public abstract AbstractNativeRingBufferBuilder<T> unsafe();

    void unsafe0() {
        unsafe = true;
    }

    public abstract AbstractNativeRingBufferBuilder<T> asserting();

    void asserting0() {
        asserting = true;
    }

    public abstract AbstractNativeRingBufferBuilder<T> notReporting();

    void notReporting0() {
        notReporting = true;
    }

    public long getLongCapacity() {
        return capacity;
    }

    public long getLongCapacityMinusOne() {
        return capacity - 1L;
    }

    public NativeByteArray getBuffer() {
        if (unsafe) {
            return new UnsafeNativeByteArray(capacity);
        }
        if (asserting) {
            if (notReporting) {
                return new AssertingSafeNativeByteArray(capacity);
            }
            return new ReportingAssertingSafeNativeByteArray(capacity);
        }
        if (notReporting) {
            return new SafeNativeByteArray(capacity);
        }
        return new ReportingSafeNativeByteArray(capacity);
    }

    public Long newLongCursor() {
        return memoryOrder.newLong();
    }
}
