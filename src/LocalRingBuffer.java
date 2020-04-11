package eu.menzani.ringbuffer;

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
        buffer = builder.getBuffer();
        gcEnabled = builder.isGCEnabled();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T next() {
        T element = buffer[writePosition];
        if (writePosition == 0) {
            writePosition = capacityMinusOne;
        } else {
            writePosition--;
        }
        return element;
    }

    @Override
    public void put() {}

    @Override
    public void put(T element) {
        buffer[writePosition] = element;
        if (writePosition == 0) {
            writePosition = capacityMinusOne;
        } else {
            writePosition--;
        }
    }

    @Override
    public T take() {
        T element = buffer[readPosition];
        if (gcEnabled) {
            buffer[readPosition] = null;
        }
        if (readPosition == 0) {
            readPosition = capacityMinusOne;
        } else {
            readPosition--;
        }
        return element;
    }

    @Override
    public void fill(T[] buffer) {
        if (readPosition >= buffer.length) {
            int i = readPosition;
            readPosition -= buffer.length;
            for (int j = 0; i > readPosition; i--) {
                buffer[j++] = this.buffer[i];
                if (gcEnabled) {
                    this.buffer[i] = null;
                }
            }
        } else {
            fillSplit(buffer);
        }
    }

    private void fillSplit(T[] buffer) {
        int i = readPosition;
        int j = 0;
        for (; i >= 0; i--) {
            buffer[j++] = this.buffer[i];
            if (gcEnabled) {
                this.buffer[i] = null;
            }
        }
        readPosition += capacity - buffer.length;
        for (i = capacityMinusOne; i > readPosition; i--) {
            buffer[j++] = this.buffer[i];
            if (gcEnabled) {
                this.buffer[i] = null;
            }
        }
    }

    @Override
    public void forEach(Consumer<T> action) {
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(buffer[i]);
            }
        } else {
            forEachSplit(action);
        }
    }

    private void forEachSplit(Consumer<T> action) {
        for (int i = readPosition; i >= 0; i--) {
            action.accept(buffer[i]);
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            action.accept(buffer[i]);
        }
    }

    @Override
    public boolean contains(T element) {
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (buffer[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element);
    }

    private boolean containsSplit(T element) {
        for (int i = readPosition; i >= 0; i--) {
            if (buffer[i].equals(element)) {
                return true;
            }
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            if (buffer[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        if (writePosition < readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                builder.append(buffer[i].toString());
                builder.append(", ");
            }
        } else {
            toStringSplit(builder);
        }
        builder.setLength(builder.length() - 2);
        builder.append(']');
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder) {
        for (int i = readPosition; i >= 0; i--) {
            builder.append(buffer[i].toString());
            builder.append(", ");
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            builder.append(buffer[i].toString());
            builder.append(", ");
        }
    }
}
