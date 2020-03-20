package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.StringJoiner;

class AtomicWriteRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;
    private final boolean gcEnabled;
    private final BusyWaitStrategy readBusyWaitStrategy;

    private int readPosition;
    private final LazyVolatileInteger writePosition = new LazyVolatileInteger();

    private int newWritePosition;

    AtomicWriteRingBuffer(RingBufferBuilder<?> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.newBuffer();
        gcEnabled = builder.isGCEnabled();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T next() {
        int writePosition = this.writePosition.getPlain();
        if (writePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition = writePosition + 1;
        }
        return (T) buffer[writePosition];
    }

    @Override
    public void put() {
        writePosition.set(newWritePosition);
    }

    @Override
    public synchronized void put(T element) {
        int writePosition = this.writePosition.getPlain();
        buffer[writePosition] = element;
        if (writePosition == capacityMinusOne) {
            this.writePosition.set(0);
        } else {
            this.writePosition.set(writePosition + 1);
        }
    }

    @Override
    public T take() {
        int readPosition = this.readPosition;
        readBusyWaitStrategy.reset();
        while (writePosition.get() == readPosition) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == capacityMinusOne) {
            this.readPosition = 0;
        } else {
            this.readPosition++;
        }
        Object element = buffer[readPosition];
        if (gcEnabled) {
            buffer[readPosition] = null;
        }
        return (T) element;
    }

    @Override
    public void fill(Array<T> buffer) {
        int bufferSize = buffer.getCapacity();
        readBusyWaitStrategy.reset();
        while (size() < bufferSize) {
            readBusyWaitStrategy.tick();
        }
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
            splitTake(buffer, bufferSize);
        }
    }

    private void splitTake(Array<T> buffer, int bufferSize) {
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
        int writePosition = this.writePosition.get();
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
        int writePosition = this.writePosition.get();
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return writePosition.get() == readPosition;
    }

    @Override
    public String toString() {
        int writePosition = this.writePosition.get();
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
