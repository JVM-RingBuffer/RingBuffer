package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

import static eu.menzani.ringbuffer.RingBufferHelper.*;

class ConcurrentGCRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;

    private int readPosition;
    private final Integer writePosition;

    ConcurrentGCRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writePosition = builder.newCursor();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public Object getReadMonitor() {
        return readBusyWaitStrategy;
    }

    @Override
    public T next() {
        return shouldNotBeGarbageCollected();
    }

    @Override
    public void put() {
        shouldNotBeGarbageCollected();
    }

    @Override
    public synchronized void put(T element) {
        int writePosition = this.writePosition.getPlain();
        buffer[writePosition] = element;
        if (writePosition == 0) {
            this.writePosition.set(capacityMinusOne);
        } else {
            this.writePosition.set(writePosition - 1);
        }
    }

    @Override
    public T take() {
        int readPosition;
        synchronized (readBusyWaitStrategy) {
            readPosition = this.readPosition;
            readBusyWaitStrategy.reset();
            while (writePosition.get() == readPosition) {
                readBusyWaitStrategy.tick();
            }
            if (readPosition == 0) {
                this.readPosition = capacityMinusOne;
            } else {
                this.readPosition--;
            }
        }
        T element = buffer[readPosition];
        buffer[readPosition] = null;
        return element;
    }

    @Override
    public void takeBatch(int size) {
        readBusyWaitStrategy.reset();
        while (size() < size) {
            readBusyWaitStrategy.tick();
        }
    }

    @Override
    public T takePlain() {
        T element = buffer[readPosition];
        buffer[readPosition] = null;
        if (readPosition == 0) {
            readPosition = capacityMinusOne;
        } else {
            readPosition--;
        }
        return element;
    }

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = getReadPosition();
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                action.accept(buffer[i]);
            }
        } else {
            forEachSplit(action, readPosition, writePosition);
        }
    }

    private void forEachSplit(Consumer<T> action, int readPosition, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            action.accept(buffer[i]);
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            action.accept(buffer[i]);
        }
    }

    @Override
    public boolean contains(T element) {
        int readPosition = getReadPosition();
        int writePosition = this.writePosition.get();
        if (writePosition <= readPosition) {
            for (int i = readPosition; i > writePosition; i--) {
                if (buffer[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, readPosition, writePosition);
    }

    private boolean containsSplit(T element, int readPosition, int writePosition) {
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
        return size(getReadPosition(), writePosition.get());
    }

    private int size(int readPosition, int writePosition) {
        if (writePosition <= readPosition) {
            return readPosition - writePosition;
        }
        return capacity - (writePosition - readPosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(getReadPosition(), writePosition.get());
    }

    private boolean isEmpty(int readPosition, int writePosition) {
        return writePosition == readPosition;
    }

    @Override
    public String toString() {
        int readPosition = getReadPosition();
        int writePosition = this.writePosition.get();
        if (isEmpty(readPosition, writePosition)) {
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
            toStringSplit(builder, readPosition, writePosition);
        }
        builder.setLength(builder.length() - 2);
        builder.append(']');
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder, int readPosition, int writePosition) {
        for (int i = readPosition; i >= 0; i--) {
            builder.append(buffer[i].toString());
            builder.append(", ");
        }
        for (int i = capacityMinusOne; i > writePosition; i--) {
            builder.append(buffer[i].toString());
            builder.append(", ");
        }
    }

    private int getReadPosition() {
        synchronized (readBusyWaitStrategy) {
            return readPosition;
        }
    }
}
