package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.builder.MarshallingBlockingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.MarshallingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.DirectMarshallingBlockingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.DirectMarshallingRingBufferBuilder;

public class BuilderProxy {
    public static AtomicReadMarshallingRingBuffer atomicReadMarshallingRingBuffer(MarshallingRingBufferBuilder builder) {
        return new AtomicReadMarshallingRingBuffer(builder);
    }

    public static Class<?> atomicReadMarshallingRingBuffer() {
        return AtomicReadMarshallingRingBuffer.class;
    }

    public static AtomicReadMarshallingBlockingRingBuffer atomicReadMarshallingBlockingRingBuffer(MarshallingBlockingRingBufferBuilder builder) {
        return new AtomicReadMarshallingBlockingRingBuffer(builder);
    }

    public static Class<?> atomicReadMarshallingBlockingRingBuffer() {
        return AtomicReadMarshallingBlockingRingBuffer.class;
    }

    public static AtomicReadDirectMarshallingRingBuffer atomicReadDirectMarshallingRingBuffer(DirectMarshallingRingBufferBuilder builder) {
        return new AtomicReadDirectMarshallingRingBuffer(builder);
    }

    public static Class<?> atomicReadDirectMarshallingRingBuffer() {
        return AtomicReadDirectMarshallingRingBuffer.class;
    }

    public static AtomicReadDirectMarshallingBlockingRingBuffer atomicReadDirectMarshallingBlockingRingBuffer(DirectMarshallingBlockingRingBufferBuilder builder) {
        return new AtomicReadDirectMarshallingBlockingRingBuffer(builder);
    }

    public static Class<?> atomicReadDirectMarshallingBlockingRingBuffer() {
        return AtomicReadDirectMarshallingBlockingRingBuffer.class;
    }

    public static AtomicWriteMarshallingRingBuffer atomicWriteMarshallingRingBuffer(MarshallingRingBufferBuilder builder) {
        return new AtomicWriteMarshallingRingBuffer(builder);
    }

    public static Class<?> atomicWriteMarshallingRingBuffer() {
        return AtomicWriteMarshallingRingBuffer.class;
    }

    public static AtomicWriteMarshallingBlockingRingBuffer atomicWriteMarshallingBlockingRingBuffer(MarshallingBlockingRingBufferBuilder builder) {
        return new AtomicWriteMarshallingBlockingRingBuffer(builder);
    }

    public static Class<?> atomicWriteMarshallingBlockingRingBuffer() {
        return AtomicWriteMarshallingBlockingRingBuffer.class;
    }

    public static AtomicWriteDirectMarshallingRingBuffer atomicWriteDirectMarshallingRingBuffer(DirectMarshallingRingBufferBuilder builder) {
        return new AtomicWriteDirectMarshallingRingBuffer(builder);
    }

    public static Class<?> atomicWriteDirectMarshallingRingBuffer() {
        return AtomicWriteDirectMarshallingRingBuffer.class;
    }

    public static AtomicWriteDirectMarshallingBlockingRingBuffer atomicWriteDirectMarshallingBlockingRingBuffer(DirectMarshallingBlockingRingBufferBuilder builder) {
        return new AtomicWriteDirectMarshallingBlockingRingBuffer(builder);
    }

    public static Class<?> atomicWriteDirectMarshallingBlockingRingBuffer() {
        return AtomicWriteDirectMarshallingBlockingRingBuffer.class;
    }

    public static ConcurrentMarshallingRingBuffer concurrentMarshallingRingBuffer(MarshallingRingBufferBuilder builder) {
        return new ConcurrentMarshallingRingBuffer(builder);
    }

    public static Class<?> concurrentMarshallingRingBuffer() {
        return ConcurrentMarshallingRingBuffer.class;
    }

    public static ConcurrentMarshallingBlockingRingBuffer concurrentMarshallingBlockingRingBuffer(MarshallingBlockingRingBufferBuilder builder) {
        return new ConcurrentMarshallingBlockingRingBuffer(builder);
    }

    public static Class<?> concurrentMarshallingBlockingRingBuffer() {
        return ConcurrentMarshallingBlockingRingBuffer.class;
    }

    public static ConcurrentDirectMarshallingRingBuffer concurrentDirectMarshallingRingBuffer(DirectMarshallingRingBufferBuilder builder) {
        return new ConcurrentDirectMarshallingRingBuffer(builder);
    }

    public static Class<?> concurrentDirectMarshallingRingBuffer() {
        return ConcurrentDirectMarshallingRingBuffer.class;
    }

    public static ConcurrentDirectMarshallingBlockingRingBuffer concurrentDirectMarshallingBlockingRingBuffer(DirectMarshallingBlockingRingBufferBuilder builder) {
        return new ConcurrentDirectMarshallingBlockingRingBuffer(builder);
    }

    public static Class<?> concurrentDirectMarshallingBlockingRingBuffer() {
        return ConcurrentDirectMarshallingBlockingRingBuffer.class;
    }

    public static VolatileMarshallingRingBuffer volatileMarshallingRingBuffer(MarshallingRingBufferBuilder builder) {
        return new VolatileMarshallingRingBuffer(builder);
    }

    public static Class<?> volatileMarshallingRingBuffer() {
        return VolatileMarshallingRingBuffer.class;
    }

    public static VolatileMarshallingBlockingRingBuffer volatileMarshallingBlockingRingBuffer(MarshallingBlockingRingBufferBuilder builder) {
        return new VolatileMarshallingBlockingRingBuffer(builder);
    }

    public static Class<?> volatileMarshallingBlockingRingBuffer() {
        return VolatileMarshallingBlockingRingBuffer.class;
    }

    public static VolatileDirectMarshallingRingBuffer volatileDirectMarshallingRingBuffer(DirectMarshallingRingBufferBuilder builder) {
        return new VolatileDirectMarshallingRingBuffer(builder);
    }

    public static Class<?> volatileDirectMarshallingRingBuffer() {
        return VolatileDirectMarshallingRingBuffer.class;
    }

    public static VolatileDirectMarshallingBlockingRingBuffer volatileDirectMarshallingBlockingRingBuffer(DirectMarshallingBlockingRingBufferBuilder builder) {
        return new VolatileDirectMarshallingBlockingRingBuffer(builder);
    }

    public static Class<?> volatileDirectMarshallingBlockingRingBuffer() {
        return VolatileDirectMarshallingBlockingRingBuffer.class;
    }
}
