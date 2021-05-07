package org.ringbuffer.object;

import eu.menzani.concurrent.ThreadLocal;
import eu.menzani.struct.Arrays;
import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

@Contended
public class ConcurrentOverwritingGCRingBuffer<T> implements OverwritingRingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;

    private int readPosition;
    private int writePosition;
    private boolean isFull;

    public ConcurrentOverwritingGCRingBuffer(int capacity) {
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
        buffer = Arrays.allocateGeneric(capacity);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public synchronized void put(T element) {
        int writePosition = this.writePosition;
        int readPosition = this.readPosition;
        if (writePosition == readPosition) {
            if (isFull) {
                if (readPosition == 0) {
                    this.readPosition = capacityMinusOne;
                } else {
                    this.readPosition--;
                }
            } else {
                isFull = true;
            }
        }
        if (writePosition == 0) {
            this.writePosition = capacityMinusOne;
        } else {
            this.writePosition--;
        }
        buffer[writePosition] = element;
    }

    @Override
    public T take() {
        return take(HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    @Override
    public T take(@ThreadLocal BusyWaitStrategy busyWaitStrategy) {
        busyWaitStrategy.reset();
        while (true) {
            synchronized (this) {
                int readPosition = this.readPosition;
                if (writePosition != readPosition || isFull) {
                    isFull = false;
                    if (readPosition == 0) {
                        this.readPosition = capacityMinusOne;
                    } else {
                        this.readPosition--;
                    }
                    T element = buffer[readPosition];
                    buffer[readPosition] = null;
                    return element;
                }
            }
            busyWaitStrategy.tick();
        }
    }

    @Override
    public synchronized int size() {
        int writePosition = this.writePosition;
        int readPosition = this.readPosition;
        if (writePosition < readPosition) {
            return readPosition - writePosition;
        }
        if (writePosition > readPosition) {
            return capacity - (writePosition - readPosition);
        }
        if (isFull) {
            return capacity;
        }
        return 0;
    }

    @Override
    public synchronized boolean isEmpty() {
        return writePosition == readPosition && !isFull;
    }

    @Override
    public synchronized boolean isNotEmpty() {
        return writePosition != readPosition || isFull;
    }
}
