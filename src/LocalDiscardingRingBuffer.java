package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;

import java.util.StringJoiner;

class LocalDiscardingRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;
    private final boolean gcEnabled;
    private final T dummyElement;

    private int readPosition;
    private int writePosition;

    LocalDiscardingRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.newBuffer();
        gcEnabled = builder.isGCEnabled();
        dummyElement = builder.getDummyElement();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T next() {
        int oldWritePosition = writePosition;
        if (writePosition == capacityMinusOne) {
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
    public void put(T element) {
        int oldWritePosition = writePosition;
        if (writePosition == capacityMinusOne) {
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
    public void fill(Array<T> buffer) {
        int bufferSize = buffer.getCapacity();
        if (readPosition < capacity - bufferSize) {
            int i = readPosition;
            readPosition += bufferSize;
            for (int j = 0; i < readPosition; i++) {
                buffer.setElement(j++, (T) this.buffer[i]);
                if (gcEnabled) {
                    this.buffer[i] = null;
                }
            }
        } else {
            splitFill(buffer, bufferSize);
        }
    }

    private void splitFill(Array<T> buffer, int bufferSize) {
        int j = 0;
        for (int i = readPosition; i < capacity; i++) {
            buffer.setElement(j++, (T) this.buffer[i]);
            if (gcEnabled) {
                this.buffer[i] = null;
            }
        }
        readPosition += bufferSize - capacity;
        for (int i = 0; i < readPosition; i++) {
            buffer.setElement(j++, (T) this.buffer[i]);
            if (gcEnabled) {
                this.buffer[i] = null;
            }
        }
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
