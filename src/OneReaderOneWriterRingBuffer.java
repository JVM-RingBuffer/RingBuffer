package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public class OneReaderOneWriterRingBuffer<T> implements RingBuffer<T>, PrefilledRingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final int capacityMinusOne;

    private int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    public OneReaderOneWriterRingBuffer(int capacity) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
    }

    public OneReaderOneWriterRingBuffer(int capacity, Supplier<? extends T> filler) {
        this(capacity);

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
        int writePosition = this.writePosition;
        if (writePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition = writePosition + 1;
        }
        return (T) buffer[writePosition];
    }

    @Override
    public void commit() {
        writePosition = newWritePosition;
    }

    @Override
    public void put(T element) {
        int newWritePosition = writePosition;
        if (newWritePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition++;
        }
        buffer[writePosition] = element;
        writePosition = newWritePosition;
    }

    @Override
    public T take() {
        int oldReadPosition = readPosition;
        while (writePosition == oldReadPosition) {
            Thread.onSpinWait();
        }
        if (oldReadPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition++;
        }
        return (T) buffer[oldReadPosition];
    }

    @Override
    public int size() {
        int writePosition = this.writePosition;
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
