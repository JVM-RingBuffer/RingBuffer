package eu.menzani.ringbuffer.marshalling;

import eu.menzani.ringbuffer.builder.HeapBlockingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.HeapRingBufferBuilder;
import eu.menzani.ringbuffer.builder.NativeBlockingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.NativeRingBufferBuilder;

public class BuilderProxy {
    public static AtomicReadHeapRingBuffer atomicReadHeapRingBuffer(HeapRingBufferBuilder builder) {
        return new AtomicReadHeapRingBuffer(builder);
    }

    public static Class<?> atomicReadHeapRingBuffer() {
        return AtomicReadHeapRingBuffer.class;
    }

    public static AtomicReadHeapBlockingRingBuffer atomicReadHeapBlockingRingBuffer(HeapBlockingRingBufferBuilder builder) {
        return new AtomicReadHeapBlockingRingBuffer(builder);
    }

    public static Class<?> atomicReadHeapBlockingRingBuffer() {
        return AtomicReadHeapBlockingRingBuffer.class;
    }

    public static AtomicReadNativeRingBuffer atomicReadNativeRingBuffer(NativeRingBufferBuilder builder) {
        return new AtomicReadNativeRingBuffer(builder);
    }

    public static Class<?> atomicReadNativeRingBuffer() {
        return AtomicReadNativeRingBuffer.class;
    }

    public static AtomicReadNativeBlockingRingBuffer atomicReadNativeBlockingRingBuffer(NativeBlockingRingBufferBuilder builder) {
        return new AtomicReadNativeBlockingRingBuffer(builder);
    }

    public static Class<?> atomicReadNativeBlockingRingBuffer() {
        return AtomicReadNativeBlockingRingBuffer.class;
    }

    public static AtomicWriteHeapRingBuffer atomicWriteHeapRingBuffer(HeapRingBufferBuilder builder) {
        return new AtomicWriteHeapRingBuffer(builder);
    }

    public static Class<?> atomicWriteHeapRingBuffer() {
        return AtomicWriteHeapRingBuffer.class;
    }

    public static AtomicWriteHeapBlockingRingBuffer atomicWriteHeapBlockingRingBuffer(HeapBlockingRingBufferBuilder builder) {
        return new AtomicWriteHeapBlockingRingBuffer(builder);
    }

    public static Class<?> atomicWriteHeapBlockingRingBuffer() {
        return AtomicWriteHeapBlockingRingBuffer.class;
    }

    public static AtomicWriteNativeRingBuffer atomicWriteNativeRingBuffer(NativeRingBufferBuilder builder) {
        return new AtomicWriteNativeRingBuffer(builder);
    }

    public static Class<?> atomicWriteNativeRingBuffer() {
        return AtomicWriteNativeRingBuffer.class;
    }

    public static AtomicWriteNativeBlockingRingBuffer atomicWriteNativeBlockingRingBuffer(NativeBlockingRingBufferBuilder builder) {
        return new AtomicWriteNativeBlockingRingBuffer(builder);
    }

    public static Class<?> atomicWriteNativeBlockingRingBuffer() {
        return AtomicWriteNativeBlockingRingBuffer.class;
    }

    public static ConcurrentHeapRingBuffer concurrentHeapRingBuffer(HeapRingBufferBuilder builder) {
        return new ConcurrentHeapRingBuffer(builder);
    }

    public static Class<?> concurrentHeapRingBuffer() {
        return ConcurrentHeapRingBuffer.class;
    }

    public static ConcurrentHeapBlockingRingBuffer concurrentHeapBlockingRingBuffer(HeapBlockingRingBufferBuilder builder) {
        return new ConcurrentHeapBlockingRingBuffer(builder);
    }

    public static Class<?> concurrentHeapBlockingRingBuffer() {
        return ConcurrentHeapBlockingRingBuffer.class;
    }

    public static ConcurrentNativeRingBuffer concurrentNativeRingBuffer(NativeRingBufferBuilder builder) {
        return new ConcurrentNativeRingBuffer(builder);
    }

    public static Class<?> concurrentNativeRingBuffer() {
        return ConcurrentNativeRingBuffer.class;
    }

    public static ConcurrentNativeBlockingRingBuffer concurrentNativeBlockingRingBuffer(NativeBlockingRingBufferBuilder builder) {
        return new ConcurrentNativeBlockingRingBuffer(builder);
    }

    public static Class<?> concurrentNativeBlockingRingBuffer() {
        return ConcurrentNativeBlockingRingBuffer.class;
    }

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

    public static VolatileNativeRingBuffer volatileNativeRingBuffer(NativeRingBufferBuilder builder) {
        return new VolatileNativeRingBuffer(builder);
    }

    public static Class<?> volatileNativeRingBuffer() {
        return VolatileNativeRingBuffer.class;
    }

    public static VolatileNativeBlockingRingBuffer volatileNativeBlockingRingBuffer(NativeBlockingRingBufferBuilder builder) {
        return new VolatileNativeBlockingRingBuffer(builder);
    }

    public static Class<?> volatileNativeBlockingRingBuffer() {
        return VolatileNativeBlockingRingBuffer.class;
    }
}
