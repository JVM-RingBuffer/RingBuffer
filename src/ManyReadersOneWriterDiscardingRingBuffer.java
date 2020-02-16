package eu.menzani.ringbuffer;

import java.util.function.Supplier;

public class ManyReadersOneWriterDiscardingRingBuffer<T> implements RingBuffer<T>, PrefilledRingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final int capacityMinusOne;
    private final T dummyElement;
    private final boolean prefilled;

    private volatile int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    private ManyReadersOneWriterDiscardingRingBuffer(int capacity, T dummyElement, boolean prefilled) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
        this.dummyElement = dummyElement;
        this.prefilled = prefilled;
    }

    public ManyReadersOneWriterDiscardingRingBuffer(int capacity, T dummyElement) {
        this(capacity, dummyElement, false);
    }

    public ManyReadersOneWriterDiscardingRingBuffer(int capacity, Supplier<T> filler) {
        this(capacity, filler.get(), true);

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
        if (readPosition == newWritePosition) {
            return dummyElement;
        }
        return (T) buffer[writePosition];
    }

    @Override
    public void endPut() {
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
        if (readPosition != newWritePosition) {
            buffer[writePosition] = element;
            writePosition = newWritePosition;
        }
    }

    @Override
    public synchronized T take() {
        int oldReadPosition = readPosition;
        while (writePosition == oldReadPosition) {
            Thread.onSpinWait();
        }
        if (oldReadPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition = oldReadPosition + 1;
        }
        if (prefilled) {
            return (T) buffer[oldReadPosition];
        }
        Object element = buffer[oldReadPosition];
        buffer[oldReadPosition] = null;
        return (T) element;
    }

    @Override
    public int size() {
        int writePosition = this.writePosition;
        int readPosition = this.readPosition;
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return writePosition == readPosition;
    }

    @Override
    public boolean isNotEmpty() {
        return writePosition != readPosition;
    }
}
