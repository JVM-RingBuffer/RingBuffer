package eu.menzani.ringbuffer.builder;

import eu.menzani.ringbuffer.marshalling.array.ByteArray;
import eu.menzani.ringbuffer.marshalling.array.DirectByteArray;
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

    public static int getCapacity(AbstractMarshallingRingBufferBuilder<?> builder) {
        return builder.getCapacity();
    }

    public static int getCapacityMinusOne(AbstractMarshallingRingBufferBuilder<?> builder) {
        return builder.getCapacityMinusOne();
    }

    public static ByteArray getBuffer(AbstractMarshallingRingBufferBuilder<?> builder) {
        return builder.getBuffer();
    }

    public static Integer newCursor(AbstractMarshallingRingBufferBuilder<?> builder) {
        return builder.newCursor();
    }

    public static long getCapacity(AbstractDirectMarshallingRingBufferBuilder<?> builder) {
        return builder.getCapacity();
    }

    public static long getCapacityMinusOne(AbstractDirectMarshallingRingBufferBuilder<?> builder) {
        return builder.getCapacityMinusOne();
    }

    public static DirectByteArray getBuffer(AbstractDirectMarshallingRingBufferBuilder<?> builder) {
        return builder.getBuffer();
    }

    public static Long newCursor(AbstractDirectMarshallingRingBufferBuilder<?> builder) {
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
