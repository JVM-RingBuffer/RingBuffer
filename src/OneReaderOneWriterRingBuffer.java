package eu.menzani.ringbuffer;

public final class OneReaderOneWriterRingBuffer<T> {
    private final Object[] buffer;
    private final int capacityMinusOne;
    private int readPosition;
    private volatile int writePosition;

    public OneReaderOneWriterRingBuffer(int capacity) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        capacityMinusOne = --capacity;
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
}
