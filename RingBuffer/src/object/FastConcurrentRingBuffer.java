package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import eu.menzani.atomic.AtomicInt;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

@Contended
class FastConcurrentRingBuffer<T> extends FastRingBuffer<T> {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        final Class<?> clazz = FastConcurrentRingBuffer.class;
        READ_POSITION = Lang.objectFieldOffset(clazz, "readPosition");
        WRITE_POSITION = Lang.objectFieldOffset(clazz, "writePosition");
    }

    private final int capacityMinusOne;
    private final T[] buffer;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastConcurrentRingBuffer(RingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public void put(T element) {
        AtomicArray.setRelease(buffer, AtomicInt.getAndIncrementVolatile(this, WRITE_POSITION) & capacityMinusOne, element);
    }

    @Override
    public T take() {
        return take(HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        T element;
        int readPosition = AtomicInt.getAndIncrementVolatile(this, READ_POSITION) & capacityMinusOne;
        busyWaitStrategy.reset();
        while ((element = AtomicArray.getAcquire(buffer, readPosition)) == null) {
            busyWaitStrategy.tick();
        }
        AtomicArray.setOpaque(buffer, readPosition, null);
        return element;
    }
}
