package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import eu.menzani.atomic.AtomicInt;
import eu.menzani.lang.Lang;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;

@Contended
class LockfreeAtomicWriteRingBuffer<T> implements LockfreeRingBuffer<T> {
    private static final long WRITE_POSITION = Lang.objectFieldOffset(LockfreeAtomicWriteRingBuffer.class, "writePosition");

    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    LockfreeAtomicWriteRingBuffer(LockfreeRingBufferBuilder<T> builder) {
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
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
        return take(readBusyWaitStrategy);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
        T element;
        int readPosition = this.readPosition++ & capacityMinusOne;
        busyWaitStrategy.reset();
        while ((element = AtomicArray.getAndSetVolatile(buffer, readPosition, null)) == null) {
            busyWaitStrategy.tick();
        }
        return element;
    }
}
