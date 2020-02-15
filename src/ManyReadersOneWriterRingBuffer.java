package eu.menzani.ringbuffer;

public final class ManyReadersOneWriterRingBuffer<T> {
    private final Object[] buffer;
    private final int capacityMinusOne;
    private int readPosition;
    private volatile int writePosition;

    public ManyReadersOneWriterRingBuffer(int capacity) {
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

    public synchronized T take() {
        int oldReadPosition = readPosition;
        while (writePosition == oldReadPosition) ;
        if (oldReadPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition++;
        }
        return (T) buffer[oldReadPosition];
    }
}
