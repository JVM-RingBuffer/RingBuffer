package eu.menzani.ringbuffer;

import java.util.StringJoiner;

abstract class RingBufferBase<T> implements RingBuffer<T> {
    final int capacity;
    final int capacityMinusOne;
    final Object[] buffer;
    final boolean gc;

    RingBufferBase(RingBufferBuilder<?> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.newBuffer();
        gc = builder.getGC();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean contains(T element) {
        int readPosition = getReadPosition();
        int writePosition = getWritePosition();
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
        int readPosition = getReadPosition();
        int writePosition = getWritePosition();
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return getWritePosition() == getReadPosition();
    }

    @Override
    public String toString() {
        int readPosition = getReadPosition();
        int writePosition = getWritePosition();
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

    abstract int getReadPosition();

    abstract int getWritePosition();

    int incrementWritePosition(int writePosition) {
        if (writePosition == capacityMinusOne) {
            return 0;
        }
        return ++writePosition;
    }
}
