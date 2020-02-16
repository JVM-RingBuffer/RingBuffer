package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public final class ManyReadersOneWriterRingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final int capacityMinusOne;
    private final boolean prefilled;

    private int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    private ManyReadersOneWriterRingBuffer(int capacity, boolean prefilled) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
        this.prefilled = prefilled;
    }

    public ManyReadersOneWriterRingBuffer(int capacity) {
        this(capacity, false);
    }

    public ManyReadersOneWriterRingBuffer(int capacity, Supplier<T> filler) {
        this(capacity, true);

        for (int i = 0; i < capacity; i++) {
            buffer[i] = filler.get();
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public T put() {
        int writePosition = this.writePosition;
        if (writePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition = writePosition + 1;
        }
        return (T) buffer[writePosition];
    }

    public void endPut() {
        writePosition = newWritePosition;
    }

    public void put(Object element) {
        int newWritePosition = writePosition;
        if (newWritePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition++;
        }
        buffer[writePosition] = element;
        writePosition = newWritePosition;
    }

    public synchronized T take() {
        int oldReadPosition = readPosition;
        while (writePosition == oldReadPosition) {
            Thread.onSpinWait();
        }
        if (oldReadPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition++;
        }
        if (prefilled) {
            return (T) buffer[oldReadPosition];
        }
        Object element = buffer[oldReadPosition];
        buffer[oldReadPosition] = null;
        return (T) element;
    }

    public int size() {
        int writePosition = this.writePosition;
        int readPosition;
        synchronized (this) {
            readPosition = this.readPosition;
        }
        if (writePosition >= readPosition) {
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
