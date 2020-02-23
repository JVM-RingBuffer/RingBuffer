package eu.menzani.ringbuffer;

import java.util.StringJoiner;

class LocalRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;
    private final boolean gcEnabled;

    private int readPosition;
    private int writePosition;

    LocalRingBuffer(RingBufferBuilder<?> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.newBuffer();
        gcEnabled = builder.isGCEnabled();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T next() {
        Object element = buffer[writePosition];
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
        return (T) element;
    }

    @Override
    public void put() {}

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
        if (gcEnabled) {
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
    public boolean contains(T element) {
        if (writePosition >= readPosition) {
            for (int i = readPosition; i < writePosition; i++) {
                if (buffer[i].equals(element)) {
                    return true;
                }
            }
        } else {
            for (int i = writePosition; i < readPosition; i++) {
                if (buffer[i].equals(element)) {
                    return true;
                }
            }
        }
        return false;
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

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        if (writePosition >= readPosition) {
            for (int i = readPosition; i < writePosition; i++) {
                joiner.add(buffer[i].toString());
            }
        } else {
            for (int i = writePosition; i < readPosition; i++) {
                joiner.add(buffer[i].toString());
            }
        }
        return joiner.toString();
    }
}
