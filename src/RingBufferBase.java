package eu.menzani.ringbuffer;

import java.util.StringJoiner;

abstract class RingBufferBase<T> implements RingBuffer<T> {
    final int capacity;
    final int capacityMinusOne;
    final Object[] buffer;
    final boolean gc;

    RingBufferBase(RingBufferBuilder options) {
        capacity = options.getCapacity();
        capacityMinusOne = options.getCapacityMinusOne();
        buffer = options.newBuffer();
        gc = options.getGC();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    int incrementWritePosition(int writePosition) {
        if (writePosition == capacityMinusOne) {
            return 0;
        }
        return ++writePosition;
    }

    boolean contains(int readPosition, int writePosition, Object element) {
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

    int size(int readPosition, int writePosition) {
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    String toString(int readPosition, int writePosition) {
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
