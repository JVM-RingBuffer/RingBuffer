package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import eu.menzani.atomic.AtomicBooleanArray;
import eu.menzani.atomic.AtomicInt;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;

@Contended
class LockfreeAtomicWritePrefilledRingBuffer<T> implements LockfreePrefilledRingBuffer<T> {
    private static final long WRITE_POSITION = Lang.objectFieldOffset(LockfreeAtomicWritePrefilledRingBuffer.class, "writePosition");

    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean[] positionNotModified;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    LockfreeAtomicWritePrefilledRingBuffer(LockfreePrefilledRingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        positionNotModified = builder.getPositionNotModified();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
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
        return take(readBusyWaitStrategy);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        int readPosition = this.readPosition++ & capacityMinusOne;
        busyWaitStrategy.reset();
        while (AtomicBooleanArray.getAcquire(positionNotModified, readPosition)) {
            busyWaitStrategy.tick();
        }
        AtomicBooleanArray.setPlain(positionNotModified, readPosition, true);
        return AtomicArray.getPlain(buffer, readPosition);
    }
}
