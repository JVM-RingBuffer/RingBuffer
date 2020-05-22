package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.builder.HeapBlockingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.HeapRingBufferBuilder;

public class BuilderProxy {
    public static VolatileHeapRingBuffer volatileHeapRingBuffer(HeapRingBufferBuilder builder) {
        return new VolatileHeapRingBuffer(builder);
    }

    public static Class<?> volatileHeapRingBuffer() {
        return VolatileHeapRingBuffer.class;
    }

    public static VolatileHeapBlockingRingBuffer volatileHeapBlockingRingBuffer(HeapBlockingRingBufferBuilder builder) {
        return new VolatileHeapBlockingRingBuffer(builder);
    }

    public static Class<?> volatileHeapBlockingRingBuffer() {
        return VolatileHeapBlockingRingBuffer.class;
    }
}
