package eu.menzani.ringbuffer;

public class LocalRingBuffer<T> implements RingBuffer<T>, PrefilledRingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;

    private int readPosition;
    private int writePosition;

    public LocalRingBuffer(RingBufferOptions<?> options) {
        capacity = options.getCapacity();
        capacityMinusOne = options.getCapacityMinusOne();
        buffer = options.newBuffer();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T put() {
        Object element = buffer[writePosition];
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
        return (T) element;
    }

    @Override
    public void commit() {
    }

    @Override
    public void put(T element) {
        buffer[writePosition] = element;
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
    }

    @Override
    public T take() {
        if (writePosition == readPosition) {
            return null;
        }
        Object element = buffer[readPosition];
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
