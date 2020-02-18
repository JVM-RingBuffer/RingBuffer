package eu.menzani.ringbuffer;

public class LocalDiscardingRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;
    private final T dummyElement;
    private final boolean gc;

    private int readPosition;
    private int writePosition;

    public LocalDiscardingRingBuffer(RingBufferOptions<T> options) {
        capacity = options.getCapacity();
        capacityMinusOne = options.getCapacityMinusOne();
        buffer = options.newBuffer();
        dummyElement = options.getDummyElement();
        gc = options.getGC();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T put() {
        int oldWritePosition = writePosition;
        if (oldWritePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
        if (readPosition == writePosition) {
            return dummyElement;
        }
        return (T) buffer[oldWritePosition];
    }

    @Override
    public void commit() {
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
        if (gc) {
            buffer[readPosition] = null;
        }
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
