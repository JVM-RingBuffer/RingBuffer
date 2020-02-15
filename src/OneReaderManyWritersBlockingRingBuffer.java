package eu.menzani.ringbuffer;

public final class OneReaderManyWritersBlockingRingBuffer<T> {
    private final Object[] buffer;
    private final int capacityMinusOne;
    private volatile int readPosition;
    private volatile int writePosition;

    public OneReaderManyWritersBlockingRingBuffer(int capacity) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        capacityMinusOne = --capacity;
    }

    public synchronized void put(Object element) {
        int newWritePosition = writePosition;
        if (newWritePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition++;
        }
        while (readPosition == newWritePosition) {
            Thread.onSpinWait();
        }
        buffer[writePosition] = element;
        writePosition = newWritePosition;
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
        return (T) buffer[oldReadPosition];
    }
}
