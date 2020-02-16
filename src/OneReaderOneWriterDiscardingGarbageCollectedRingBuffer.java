package eu.menzani.ringbuffer;

public final class OneReaderOneWriterDiscardingGarbageCollectedRingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final int capacityMinusOne;

    private volatile int readPosition;
    private volatile int writePosition;

    public OneReaderOneWriterDiscardingGarbageCollectedRingBuffer(int capacity) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
    }

    public int getCapacity() {
        return capacity;
    }

    public void put(Object element) {
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

    public T take() {
        int oldReadPosition = readPosition;
        while (writePosition == oldReadPosition) {
            Thread.onSpinWait();
        }
        if (oldReadPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition = oldReadPosition + 1;
        }
        Object element = buffer[oldReadPosition];
        buffer[oldReadPosition] = null;
        return (T) element;
    }

    public int size() {
        int writePosition = this.writePosition;
        int readPosition = this.readPosition;
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
