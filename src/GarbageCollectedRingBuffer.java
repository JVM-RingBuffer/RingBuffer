package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public final class GarbageCollectedRingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final int capacityMinusOne;

    private int readPosition;
    private int writePosition;

    public GarbageCollectedRingBuffer(int capacity) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
    }

    public GarbageCollectedRingBuffer(int capacity, Supplier<T> filler) {
        this(capacity);

        for (int i = 0; i < capacity; i++) {
            buffer[i] = filler.get();
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public void put(Object element) {
        buffer[writePosition] = element;
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
    }

    public T take() {
        while (writePosition == readPosition) {
            Thread.onSpinWait();
        }
        Object element = buffer[readPosition];
        if (readPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition++;
        }
        return (T) element;
    }

    public int size() {
        if (writePosition > readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    public boolean isEmpty() {
        return writePosition == readPosition;
    }

    public boolean isNotEmpty() {
        return writePosition != readPosition;
    }
}
