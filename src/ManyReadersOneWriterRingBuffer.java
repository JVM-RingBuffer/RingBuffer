package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public final class ManyReadersOneWriterRingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final int capacityMinusOne;

    private int readPosition;
    private volatile int writePosition;

    private final boolean notPrefilled;
    private int newWritePosition;

    private ManyReadersOneWriterRingBuffer(int capacity, boolean notPrefilled) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
        this.notPrefilled = notPrefilled;
    }

    public ManyReadersOneWriterRingBuffer(int capacity) {
        this(capacity, true);
    }

    public ManyReadersOneWriterRingBuffer(int capacity, Supplier<T> filler) {
        this(capacity, false);

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

    public void commit() {
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
        try {
            return (T) buffer[oldReadPosition];
        } finally {
            if (notPrefilled) {
                buffer[oldReadPosition] = null;
            }
        }
    }

    public int size() {
        int writePosition = this.writePosition;
        int readPosition;
        synchronized (this) {
            readPosition = this.readPosition;
        }
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
