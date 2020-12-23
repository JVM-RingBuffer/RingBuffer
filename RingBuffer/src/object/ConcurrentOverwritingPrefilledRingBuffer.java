package org.ringbuffer.object;

import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Contended
public class ConcurrentOverwritingPrefilledRingBuffer<T> implements PrefilledOverwritingRingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;

    private int readPosition;
    private int writePosition;
    private boolean isFull;

    @SuppressWarnings("unchecked")
    public ConcurrentOverwritingPrefilledRingBuffer(int capacity, Supplier<? extends T> filler) {
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
        buffer = (T[]) new Object[capacity];
        for (int i = 0; i < capacity; i++) {
            buffer[i] = filler.get();
        }
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T next() {
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
        return buffer[writePosition];
    }

    @Override
    public T take() {
        return take(HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    @Override
    public T take(BusyWaitStrategy busyWaitStrategy) {
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
                    return buffer[readPosition];
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
    public void takeBatch(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T takePlain() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T takeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<T> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getReadMonitor() {
        throw new UnsupportedOperationException();
    }
}
