package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;

import java.util.function.Consumer;

class LocalRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean gcEnabled;

    private int readPosition;
    private int writePosition;

    LocalRingBuffer(RingBufferBuilder<T> builder) {
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
        T element = buffer[writePosition];
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
        return element;
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
        T element = buffer[readPosition];
        if (gcEnabled) {
            buffer[readPosition] = null;
        }
        if (readPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition++;
        }
        return element;
    }

    @Override
    public void fill(Array<T> buffer) {
        int bufferSize = buffer.getCapacity();
        if (readPosition < capacity - bufferSize) {
            int i = readPosition;
            readPosition += bufferSize;
            for (int j = 0; i < readPosition; i++) {
                buffer.setElement(j++, this.buffer[i]);
                if (gcEnabled) {
                    this.buffer[i] = null;
                }
            }
        } else {
            fillSplit(buffer, bufferSize);
        }
    }

    private void fillSplit(Array<T> buffer, int bufferSize) {
        int j = 0;
        for (int i = readPosition; i < capacity; i++) {
            buffer.setElement(j++, this.buffer[i]);
            if (gcEnabled) {
                this.buffer[i] = null;
            }
        }
        readPosition += bufferSize - capacity;
        for (int i = 0; i < readPosition; i++) {
            buffer.setElement(j++, this.buffer[i]);
            if (gcEnabled) {
                this.buffer[i] = null;
            }
        }
    }

    @Override
    public void forEach(Consumer<T> action) {
        if (writePosition >= readPosition) {
            for (int i = readPosition; i < writePosition; i++) {
                action.accept(buffer[i]);
            }
        } else {
            forEachSplit(action);
        }
    }

    private void forEachSplit(Consumer<T> action) {
        for (int i = readPosition; i < capacity; i++) {
            action.accept(buffer[i]);
        }
        for (int i = 0; i < writePosition; i++) {
            action.accept(buffer[i]);
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
            return false;
        }
        return containsSplit(element);
    }

    private boolean containsSplit(T element) {
        for (int i = readPosition; i < capacity; i++) {
            if (buffer[i].equals(element)) {
                return true;
            }
        }
        for (int i = 0; i < writePosition; i++) {
            if (buffer[i].equals(element)) {
                return true;
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
        if (writePosition == readPosition) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        if (writePosition > readPosition) {
            for (int i = readPosition; i < writePosition; i++) {
                builder.append(buffer[i].toString());
                builder.append(", ");
            }
        } else {
            toStringSplit(builder);
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder) {
        for (int i = readPosition; i < capacity; i++) {
            builder.append(buffer[i].toString());
            builder.append(", ");
        }
        for (int i = 0; i < writePosition; i++) {
            builder.append(buffer[i].toString());
            builder.append(", ");
        }
    }
}
