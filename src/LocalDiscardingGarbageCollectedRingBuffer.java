package eu.menzani.ringbuffer;

public class LocalDiscardingGarbageCollectedRingBuffer<T> implements RingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private final int capacityMinusOne;

    private int readPosition;
    private int writePosition;

    public LocalDiscardingGarbageCollectedRingBuffer(int capacity) {
        if (capacity < 2) {
            throw new IllegalArgumentException("capacity must be at least 2, but is " + capacity);
        }
        buffer = new Object[capacity];
        this.capacity = capacity;
        capacityMinusOne = capacity - 1;
    }

    @Override
    public int getCapacity() {
        return capacity;
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
        buffer[readPosition] = null;
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
