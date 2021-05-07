package org.ringbuffer.object;

import eu.menzani.atomic.AtomicArray;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;

@Contended
class LockfreeVolatileRingBuffer<T> implements LockfreeRingBuffer<T> {
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    @Contended
    private int readPosition;
    @Contended
    private int writePosition;

    LockfreeVolatileRingBuffer(LockfreeRingBufferBuilder<T> builder) {
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
        AtomicArray.setRelease(buffer, writePosition++ & capacityMinusOne, element);
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
        while ((element = AtomicArray.getAcquire(buffer, readPosition)) == null) {
            busyWaitStrategy.tick();
        }
        AtomicArray.setPlain(buffer, readPosition, null);
        return element;
    }
}
