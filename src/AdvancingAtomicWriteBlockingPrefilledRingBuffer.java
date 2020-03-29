package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.Objects;
import java.util.function.Consumer;

class AdvancingAtomicWriteBlockingPrefilledRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy writeBusyWaitStrategy;

    private final LazyVolatileInteger readPosition = new LazyVolatileInteger();
    private final LazyVolatileInteger writePosition = new LazyVolatileInteger();

    private int newWritePosition;
    private int newReadPosition;

    AdvancingAtomicWriteBlockingPrefilledRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.newBuffer();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
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
        writeBusyWaitStrategy.reset();
        while (readPosition.get() == newWritePosition) {
            writeBusyWaitStrategy.tick();
        }
        return buffer[writePosition];
    }

    @Override
    public void put() {
        writePosition.set(newWritePosition);
    }

    @Override
    public synchronized void put(T element) {
        int writePosition = this.writePosition.getPlain();
        int newWritePosition;
        if (writePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition = writePosition + 1;
        }
        writeBusyWaitStrategy.reset();
        while (readPosition.get() == newWritePosition) {
            writeBusyWaitStrategy.tick();
        }
        buffer[writePosition] = element;
        this.writePosition.set(newWritePosition);
    }

    @Override
    public T take() {
        int readPosition = this.readPosition.getPlain();
        readBusyWaitStrategy.reset();
        while (writePosition.get() == readPosition) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == capacityMinusOne) {
            this.readPosition.set(0);
        } else {
            this.readPosition.set(readPosition + 1);
        }
        return buffer[readPosition];
    }

    @Override
    public void fill(Array<T> buffer) {
        int readPosition = this.readPosition.getPlain();
        int bufferSize = buffer.getCapacity();
        readBusyWaitStrategy.reset();
        while (size(readPosition) < bufferSize) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition < capacity - bufferSize) {
            newReadPosition = readPosition + bufferSize;
            for (int j = 0; readPosition < newReadPosition; readPosition++) {
                buffer.setElement(j++, this.buffer[readPosition]);
            }
        } else {
            fillSplit(readPosition, buffer, bufferSize);
        }
    }

    private int size(int readPosition) {
        int writePosition = this.writePosition.get();
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    private void fillSplit(int readPosition, Array<T> buffer, int bufferSize) {
        int j = 0;
        newReadPosition = readPosition + bufferSize - capacity;
        for (; readPosition < capacity; readPosition++) {
            buffer.setElement(j++, this.buffer[readPosition]);
        }
        for (readPosition = 0; readPosition < newReadPosition; readPosition++) {
            buffer.setElement(j++, this.buffer[readPosition]);
        }
    }

    @Override
    public void advance() {
        readPosition.set(newReadPosition);
    }

    @Override
    public void forEach(Consumer<T> action) {
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (writePosition >= readPosition) {
            for (int i = readPosition; i < writePosition; i++) {
                action.accept(buffer[i]);
            }
        } else {
            forEachSplit(action, readPosition, writePosition);
        }
    }

    private void forEachSplit(Consumer<T> action, int readPosition, int writePosition) {
        for (int i = readPosition; i < capacity; i++) {
            action.accept(buffer[i]);
        }
        for (int i = 0; i < writePosition; i++) {
            action.accept(buffer[i]);
        }
    }

    @Override
    public boolean contains(T element) {
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (writePosition >= readPosition) {
            for (int i = readPosition; i < writePosition; i++) {
                if (Objects.equals(buffer[i], element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, readPosition, writePosition);
    }

    private boolean containsSplit(T element, int readPosition, int writePosition) {
        for (int i = readPosition; i < capacity; i++) {
            if (Objects.equals(buffer[i], element)) {
                return true;
            }
        }
        for (int i = 0; i < writePosition; i++) {
            if (Objects.equals(buffer[i], element)) {
                return true;
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
        if (writePosition == readPosition) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        if (writePosition > readPosition) {
            for (int i = readPosition; i < writePosition; i++) {
                builder.append(buffer[i]);
                builder.append(", ");
            }
        } else {
            toStringSplit(builder, readPosition, writePosition);
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    private void toStringSplit(StringBuilder builder, int readPosition, int writePosition) {
        for (int i = readPosition; i < capacity; i++) {
            builder.append(buffer[i]);
            builder.append(", ");
        }
        for (int i = 0; i < writePosition; i++) {
            builder.append(buffer[i]);
            builder.append(", ");
        }
    }
}
