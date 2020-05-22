package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.builder.EmptyRingBufferBuilder;
import eu.menzani.ringbuffer.builder.PrefilledOverwritingRingBufferBuilder;
import eu.menzani.ringbuffer.builder.PrefilledRingBufferBuilder;

public class BuilderProxy {
    public static <T> AtomicReadBlockingGCRingBuffer<T> atomicReadBlockingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicReadBlockingGCRingBuffer<>(builder);
    }

    public static Class<?> atomicReadBlockingGCRingBuffer() {
        return AtomicReadBlockingGCRingBuffer.class;
    }

    public static <T> AtomicReadBlockingPrefilledRingBuffer<T> atomicReadBlockingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        return new AtomicReadBlockingPrefilledRingBuffer<>(builder);
    }

    public static Class<?> atomicReadBlockingPrefilledRingBuffer() {
        return AtomicReadBlockingPrefilledRingBuffer.class;
    }

    public static <T> AtomicReadBlockingRingBuffer<T> atomicReadBlockingRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicReadBlockingRingBuffer<>(builder);
    }

    public static Class<?> atomicReadBlockingRingBuffer() {
        return AtomicReadBlockingRingBuffer.class;
    }

    public static <T> AtomicReadDiscardingGCRingBuffer<T> atomicReadDiscardingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicReadDiscardingGCRingBuffer<>(builder);
    }

    public static Class<?> atomicReadDiscardingGCRingBuffer() {
        return AtomicReadDiscardingGCRingBuffer.class;
    }

    public static <T> AtomicReadDiscardingPrefilledRingBuffer<T> atomicReadDiscardingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        return new AtomicReadDiscardingPrefilledRingBuffer<>(builder);
    }

    public static Class<?> atomicReadDiscardingPrefilledRingBuffer() {
        return AtomicReadDiscardingPrefilledRingBuffer.class;
    }

    public static <T> AtomicReadDiscardingRingBuffer<T> atomicReadDiscardingRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicReadDiscardingRingBuffer<>(builder);
    }

    public static Class<?> atomicReadDiscardingRingBuffer() {
        return AtomicReadDiscardingRingBuffer.class;
    }

    public static <T> AtomicReadGCRingBuffer<T> atomicReadGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicReadGCRingBuffer<>(builder);
    }

    public static Class<?> atomicReadGCRingBuffer() {
        return AtomicReadGCRingBuffer.class;
    }

    public static <T> AtomicReadPrefilledRingBuffer<T> atomicReadPrefilledRingBuffer(PrefilledOverwritingRingBufferBuilder<T> builder) {
        return new AtomicReadPrefilledRingBuffer<>(builder);
    }

    public static Class<?> atomicReadPrefilledRingBuffer() {
        return AtomicReadPrefilledRingBuffer.class;
    }

    public static <T> AtomicReadRingBuffer<T> atomicReadRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicReadRingBuffer<>(builder);
    }

    public static Class<?> atomicReadRingBuffer() {
        return AtomicReadRingBuffer.class;
    }

    public static <T> AtomicWriteBlockingGCRingBuffer<T> atomicWriteBlockingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicWriteBlockingGCRingBuffer<>(builder);
    }

    public static Class<?> atomicWriteBlockingGCRingBuffer() {
        return AtomicWriteBlockingGCRingBuffer.class;
    }

    public static <T> AtomicWriteBlockingPrefilledRingBuffer<T> atomicWriteBlockingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        return new AtomicWriteBlockingPrefilledRingBuffer<>(builder);
    }

    public static Class<?> atomicWriteBlockingPrefilledRingBuffer() {
        return AtomicWriteBlockingPrefilledRingBuffer.class;
    }

    public static <T> AtomicWriteBlockingRingBuffer<T> atomicWriteBlockingRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicWriteBlockingRingBuffer<>(builder);
    }

    public static Class<?> atomicWriteBlockingRingBuffer() {
        return AtomicWriteBlockingRingBuffer.class;
    }

    public static <T> AtomicWriteDiscardingGCRingBuffer<T> atomicWriteDiscardingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicWriteDiscardingGCRingBuffer<>(builder);
    }

    public static Class<?> atomicWriteDiscardingGCRingBuffer() {
        return AtomicWriteDiscardingGCRingBuffer.class;
    }

    public static <T> AtomicWriteDiscardingPrefilledRingBuffer<T> atomicWriteDiscardingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        return new AtomicWriteDiscardingPrefilledRingBuffer<>(builder);
    }

    public static Class<?> atomicWriteDiscardingPrefilledRingBuffer() {
        return AtomicWriteDiscardingPrefilledRingBuffer.class;
    }

    public static <T> AtomicWriteDiscardingRingBuffer<T> atomicWriteDiscardingRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicWriteDiscardingRingBuffer<>(builder);
    }

    public static Class<?> atomicWriteDiscardingRingBuffer() {
        return AtomicWriteDiscardingRingBuffer.class;
    }

    public static <T> AtomicWriteGCRingBuffer<T> atomicWriteGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicWriteGCRingBuffer<>(builder);
    }

    public static Class<?> atomicWriteGCRingBuffer() {
        return AtomicWriteGCRingBuffer.class;
    }

    public static <T> AtomicWritePrefilledRingBuffer<T> atomicWritePrefilledRingBuffer(PrefilledOverwritingRingBufferBuilder<T> builder) {
        return new AtomicWritePrefilledRingBuffer<>(builder);
    }

    public static Class<?> atomicWritePrefilledRingBuffer() {
        return AtomicWritePrefilledRingBuffer.class;
    }

    public static <T> AtomicWriteRingBuffer<T> atomicWriteRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new AtomicWriteRingBuffer<>(builder);
    }

    public static Class<?> atomicWriteRingBuffer() {
        return AtomicWriteRingBuffer.class;
    }

    public static <T> ConcurrentBlockingGCRingBuffer<T> concurrentBlockingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new ConcurrentBlockingGCRingBuffer<>(builder);
    }

    public static Class<?> concurrentBlockingGCRingBuffer() {
        return ConcurrentBlockingGCRingBuffer.class;
    }

    public static <T> ConcurrentBlockingPrefilledRingBuffer<T> concurrentBlockingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        return new ConcurrentBlockingPrefilledRingBuffer<>(builder);
    }

    public static Class<?> concurrentBlockingPrefilledRingBuffer() {
        return ConcurrentBlockingPrefilledRingBuffer.class;
    }

    public static <T> ConcurrentBlockingRingBuffer<T> concurrentBlockingRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new ConcurrentBlockingRingBuffer<>(builder);
    }

    public static Class<?> concurrentBlockingRingBuffer() {
        return ConcurrentBlockingRingBuffer.class;
    }

    public static <T> ConcurrentDiscardingGCRingBuffer<T> concurrentDiscardingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new ConcurrentDiscardingGCRingBuffer<>(builder);
    }

    public static Class<?> concurrentDiscardingGCRingBuffer() {
        return ConcurrentDiscardingGCRingBuffer.class;
    }

    public static <T> ConcurrentDiscardingPrefilledRingBuffer<T> concurrentDiscardingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        return new ConcurrentDiscardingPrefilledRingBuffer<>(builder);
    }

    public static Class<?> concurrentDiscardingPrefilledRingBuffer() {
        return ConcurrentDiscardingPrefilledRingBuffer.class;
    }

    public static <T> ConcurrentDiscardingRingBuffer<T> concurrentDiscardingRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new ConcurrentDiscardingRingBuffer<>(builder);
    }

    public static Class<?> concurrentDiscardingRingBuffer() {
        return ConcurrentDiscardingRingBuffer.class;
    }

    public static <T> ConcurrentGCRingBuffer<T> concurrentGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new ConcurrentGCRingBuffer<>(builder);
    }

    public static Class<?> concurrentGCRingBuffer() {
        return ConcurrentGCRingBuffer.class;
    }

    public static <T> ConcurrentPrefilledRingBuffer<T> concurrentPrefilledRingBuffer(PrefilledOverwritingRingBufferBuilder<T> builder) {
        return new ConcurrentPrefilledRingBuffer<>(builder);
    }

    public static Class<?> concurrentPrefilledRingBuffer() {
        return ConcurrentPrefilledRingBuffer.class;
    }

    public static <T> ConcurrentRingBuffer<T> concurrentRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new ConcurrentRingBuffer<>(builder);
    }

    public static Class<?> concurrentRingBuffer() {
        return ConcurrentRingBuffer.class;
    }

    public static <T> VolatileBlockingGCRingBuffer<T> volatileBlockingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new VolatileBlockingGCRingBuffer<>(builder);
    }

    public static Class<?> volatileBlockingGCRingBuffer() {
        return VolatileBlockingGCRingBuffer.class;
    }

    public static <T> VolatileBlockingPrefilledRingBuffer<T> volatileBlockingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        return new VolatileBlockingPrefilledRingBuffer<>(builder);
    }

    public static Class<?> volatileBlockingPrefilledRingBuffer() {
        return VolatileBlockingPrefilledRingBuffer.class;
    }

    public static <T> VolatileBlockingRingBuffer<T> volatileBlockingRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new VolatileBlockingRingBuffer<>(builder);
    }

    public static Class<?> volatileBlockingRingBuffer() {
        return VolatileBlockingRingBuffer.class;
    }

    public static <T> VolatileDiscardingGCRingBuffer<T> volatileDiscardingGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new VolatileDiscardingGCRingBuffer<>(builder);
    }

    public static Class<?> volatileDiscardingGCRingBuffer() {
        return VolatileDiscardingGCRingBuffer.class;
    }

    public static <T> VolatileDiscardingPrefilledRingBuffer<T> volatileDiscardingPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        return new VolatileDiscardingPrefilledRingBuffer<>(builder);
    }

    public static Class<?> volatileDiscardingPrefilledRingBuffer() {
        return VolatileDiscardingPrefilledRingBuffer.class;
    }

    public static <T> VolatileDiscardingRingBuffer<T> volatileDiscardingRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new VolatileDiscardingRingBuffer<>(builder);
    }

    public static Class<?> volatileDiscardingRingBuffer() {
        return VolatileDiscardingRingBuffer.class;
    }

    public static <T> VolatileGCRingBuffer<T> volatileGCRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new VolatileGCRingBuffer<>(builder);
    }

    public static Class<?> volatileGCRingBuffer() {
        return VolatileGCRingBuffer.class;
    }

    public static <T> VolatilePrefilledRingBuffer<T> volatilePrefilledRingBuffer(PrefilledOverwritingRingBufferBuilder<T> builder) {
        return new VolatilePrefilledRingBuffer<>(builder);
    }

    public static Class<?> volatilePrefilledRingBuffer() {
        return VolatilePrefilledRingBuffer.class;
    }

    public static <T> VolatileRingBuffer<T> volatileRingBuffer(EmptyRingBufferBuilder<T> builder) {
        return new VolatileRingBuffer<>(builder);
    }

    public static Class<?> volatileRingBuffer() {
        return VolatileRingBuffer.class;
    }
}
