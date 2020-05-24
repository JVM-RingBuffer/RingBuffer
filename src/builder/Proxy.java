package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.array.ByteArray;
import eu.menzani.ringbuffer.marshalling.array.NativeByteArray;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.memory.Long;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

public class Proxy {
    public static int getCapacity(RingBufferBuilder<?> builder) {
        return builder.getCapacity();
    }

    public static int getCapacityMinusOne(RingBufferBuilder<?> builder) {
        return builder.getCapacityMinusOne();
    }

    public static <T> T[] getBuffer(RingBufferBuilder<T> builder) {
        return builder.getBuffer();
    }

    public static Integer newCursor(RingBufferBuilder<?> builder) {
        return builder.newCursor();
    }

    public static int getCapacity(AbstractHeapRingBufferBuilder<?> builder) {
        return builder.getCapacity();
    }

    public static int getCapacityMinusOne(AbstractHeapRingBufferBuilder<?> builder) {
        return builder.getCapacityMinusOne();
    }

    public static ByteArray getBuffer(AbstractHeapRingBufferBuilder<?> builder) {
        return builder.getBuffer();
    }

    public static Integer newCursor(AbstractHeapRingBufferBuilder<?> builder) {
        return builder.newCursor();
    }

    public static long getCapacity(AbstractNativeRingBufferBuilder<?> builder) {
        return builder.getCapacity();
    }

    public static long getCapacityMinusOne(AbstractNativeRingBufferBuilder<?> builder) {
        return builder.getCapacityMinusOne();
    }

    public static NativeByteArray getBuffer(AbstractNativeRingBufferBuilder<?> builder) {
        return builder.getBuffer();
    }

    public static Long newCursor(AbstractNativeRingBufferBuilder<?> builder) {
        return builder.newCursor();
    }

    public static BusyWaitStrategy getWriteBusyWaitStrategy(AbstractRingBufferBuilder<?> builder) {
        return builder.getWriteBusyWaitStrategy();
    }

    public static BusyWaitStrategy getReadBusyWaitStrategy(AbstractRingBufferBuilder<?> builder) {
        return builder.getReadBusyWaitStrategy();
    }

    public static <T> T getDummyElement(AbstractPrefilledRingBufferBuilder<T> builder) {
        return builder.getDummyElement();
    }
}
