package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.StringJoiner;

class AtomicReadRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;
    private final boolean gcEnabled;
    private final BusyWaitStrategy readBusyWaitStrategy;

    private final LazyVolatileInteger readPosition = new LazyVolatileInteger();
    private final LazyVolatileInteger writePosition = new LazyVolatileInteger();

    private int newWritePosition;

    AtomicReadRingBuffer(RingBufferBuilder<?> builder) {
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
    public void put(T element) {
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
        int readPosition;
        synchronized (this) {
            readPosition = this.readPosition.getPlain();
            readBusyWaitStrategy.reset();
            while (writePosition.get() == readPosition) {
                readBusyWaitStrategy.tick();
            }
            if (readPosition == capacityMinusOne) {
                this.readPosition.set(0);
            } else {
                this.readPosition.set(readPosition + 1);
            }
        }
        Object element = buffer[readPosition];
        if (gcEnabled) {
            buffer[readPosition] = null;
        }
        return (T) element;
    }

    @Override
    public void fill(Array<T> buffer) {
        int readPosition;
        int bufferSize = buffer.getCapacity();
        boolean notSplit;
        int newReadPosition;
        synchronized (this) {
            readPosition = this.readPosition.getPlain();
            readBusyWaitStrategy.reset();
            while (size(readPosition) < bufferSize) {
                readBusyWaitStrategy.tick();
            }
            notSplit = readPosition < capacity - bufferSize;
            if (notSplit) {
                newReadPosition = readPosition + bufferSize;
            } else {
                newReadPosition = readPosition + bufferSize - capacity;
            }
            this.readPosition.set(newReadPosition);
        }
        if (notSplit) {
            for (int j = 0; readPosition < newReadPosition; readPosition++) {
                buffer.setElement(j++, (T) this.buffer[readPosition]);
                if (gcEnabled) {
                    this.buffer[readPosition] = null;
                }
            }
        } else {
            splitFill(readPosition, newReadPosition, buffer);
        }
    }

    private int size(int readPosition) {
        int writePosition = this.writePosition.get();
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    private void splitFill(int readPosition, int newReadPosition, Array<T> buffer) {
        int j = 0;
        for (; readPosition < capacity; readPosition++) {
            buffer.setElement(j++, (T) this.buffer[readPosition]);
            if (gcEnabled) {
                this.buffer[readPosition] = null;
            }
        }
        for (readPosition = 0; readPosition < newReadPosition; readPosition++) {
            buffer.setElement(j++, (T) this.buffer[readPosition]);
            if (gcEnabled) {
                this.buffer[readPosition] = null;
            }
        }
    }

    @Override
    public boolean contains(T element) {
        int readPosition = this.readPosition.get();
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
        return size(readPosition.get());
    }

    @Override
    public boolean isEmpty() {
        return writePosition.get() == readPosition.get();
    }

    @Override
    public String toString() {
        int readPosition = this.readPosition.get();
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
