package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public class LocalDiscardingRingBuffer<T> implements RingBuffer<T>, PrefilledRingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final int capacityMinusOne;
    private final T dummyElement;

    private int readPosition;
    private int writePosition;

    public LocalDiscardingRingBuffer(int capacity, T dummyElement) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
        this.dummyElement = dummyElement;
    }

    public LocalDiscardingRingBuffer(int capacity, Supplier<? extends T> filler) {
        this(capacity, filler.get());

        for (int i = 0; i < capacity; i++) {
            buffer[i] = filler.get();
        }
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T put() {
        int oldWritePosition = writePosition;
        if (oldWritePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
        if (readPosition == writePosition) {
            return dummyElement;
        }
        return (T) buffer[oldWritePosition];
    }

    @Override
    public void commit() {
    }

    @Override
    public void put(T element) {
        int oldWritePosition = writePosition;
        if (oldWritePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
        if (readPosition != writePosition) {
            buffer[oldWritePosition] = element;
        }
    }

    @Override
    public T take() {
        if (writePosition == readPosition) {
            return null;
        }
        Object element = buffer[readPosition];
        if (readPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition++;
        }
        return (T) element;
    }

    @Override
    public int size() {
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return writePosition == readPosition;
    }
}
