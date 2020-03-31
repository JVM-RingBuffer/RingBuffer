package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.memory.BooleanArray;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

class AtomicWriteRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean gcEnabled;
    private final BusyWaitStrategy readBusyWaitStrategy;

    private int readPosition;
    private final Integer writePosition;

    private final BooleanArray flags;

    AtomicWriteRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.newBuffer();
        gcEnabled = builder.isGCEnabled();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePosition = builder.newCursor();
        flags = builder.newFlagArray();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int nextKey() {
        return writePosition.getAndDecrement() & capacityMinusOne;
    }

    @Override
    public T next(int key) {
        return buffer[key];
    }

    @Override
    public void put(int key) {
        flags.setTrue(key);
    }

    @Override
    public void put(T element) {
        int writePosition = this.writePosition.getAndDecrement() & capacityMinusOne;
        buffer[writePosition] = element;
        flags.setTrue(writePosition);
    }

    @Override
    public T take() {
        readBusyWaitStrategy.reset();
        while (flags.isFalse(readPosition)) {
            readBusyWaitStrategy.tick();
        }
        flags.setFalsePlain(readPosition);
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
    public void fill(Array<T> buffer) {
        int bufferSize = buffer.getCapacity();
        readBusyWaitStrategy.reset();
        while (size() < bufferSize) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition >= bufferSize) {
            int i = readPosition;
            readPosition -= bufferSize;
            for (int j = 0; i > readPosition; i--) {
                readBusyWaitStrategy.reset();
                while (flags.isFalse(i)) {
                    readBusyWaitStrategy.tick();
                }
                flags.setFalsePlain(i);
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
        for (int i = readPosition; i >= 0; i--) {
            readBusyWaitStrategy.reset();
            while (flags.isFalse(i)) {
                readBusyWaitStrategy.tick();
            }
            flags.setFalsePlain(i);
            buffer.setElement(j++, this.buffer[i]);
            if (gcEnabled) {
                this.buffer[i] = null;
            }
        }
        readPosition += capacity - bufferSize;
        for (int i = capacityMinusOne; i > readPosition; i--) {
            readBusyWaitStrategy.reset();
            while (flags.isFalse(i)) {
                readBusyWaitStrategy.tick();
            }
            flags.setFalsePlain(i);
            buffer.setElement(j++, this.buffer[i]);
            if (gcEnabled) {
                this.buffer[i] = null;
            }
        }
    }

    @Override
    public void forEach(Consumer<T> action) {
        int writePosition = this.writePosition.get() & capacityMinusOne;
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(buffer[i]);
            }
        } else {
            forEachSplit(action, writePosition);
        }
    }

    private void forEachSplit(Consumer<T> action, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            action.accept(buffer[i]);
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            action.accept(buffer[i]);
        }
    }

    @Override
    public boolean contains(T element) {
        int writePosition = this.writePosition.get() & capacityMinusOne;
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (buffer[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, writePosition);
    }

    private boolean containsSplit(T element, int writePosition) {
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
        int writePosition = this.writePosition.get() & capacityMinusOne;
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return (writePosition.get() & capacityMinusOne) == readPosition;
    }

    @Override
    public String toString() {
        int writePosition = this.writePosition.get() & capacityMinusOne;
        if (writePosition == readPosition) {
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
            toStringSplit(builder, writePosition);
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder, int writePosition) {
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
