package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import eu.menzani.atomic.AtomicBooleanArray;
import eu.menzani.atomic.AtomicInt;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

@Contended
class FastConcurrentPrefilledRingBuffer<T> extends FastPrefilledRingBuffer<T> {
    private static final long READ_POSITION, WRITE_POSITION;

    static {
        final Class<?> clazz = FastConcurrentPrefilledRingBuffer.class;
        READ_POSITION = Lang.objectFieldOffset(clazz, "readPosition");
        WRITE_POSITION = Lang.objectFieldOffset(clazz, "writePosition");
    }

    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean[] positionNotModified;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    FastConcurrentPrefilledRingBuffer(PrefilledRingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        positionNotModified = builder.getPositionNotModified();
    }

    @Override
    public int getCapacity() {
        return buffer.length;
    }

    @Override
    public int nextKey() {
        return AtomicInt.getAndIncrementVolatile(this, WRITE_POSITION) & capacityMinusOne;
    }

    @Override
    public T next(int key) {
        return AtomicArray.getPlain(buffer, key);
    }

    @Override
    public void put(int key) {
        AtomicBooleanArray.setRelease(positionNotModified, key, false);
    }

    @Override
    public T take() {
        return take(HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        int readPosition = AtomicInt.getAndIncrementVolatile(this, READ_POSITION) & capacityMinusOne;
        busyWaitStrategy.reset();
        while (AtomicBooleanArray.getAcquire(positionNotModified, readPosition)) {
            busyWaitStrategy.tick();
        }
        AtomicBooleanArray.setOpaque(positionNotModified, readPosition, true);
        return AtomicArray.getPlain(buffer, readPosition);
    }
}
