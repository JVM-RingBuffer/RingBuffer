package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.StringJoiner;
import java.util.function.Consumer;

class AtomicReadBlockingOrDiscardingRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean gcEnabled;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final boolean discarding;
    private final BusyWaitStrategy writeBusyWaitStrategy;
    private final T dummyElement;

    private final LazyVolatileInteger readPosition = new LazyVolatileInteger();
    private final LazyVolatileInteger writePosition = new LazyVolatileInteger();

    private int newWritePosition;

    AtomicReadBlockingOrDiscardingRingBuffer(RingBufferBuilder<T> builder, boolean discarding) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.newBuffer();
        gcEnabled = builder.isGCEnabled();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        this.discarding = discarding;
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
        if (discarding) {
            dummyElement = builder.getDummyElement();
        } else {
            dummyElement = null;
        }
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
        if (isBufferNotFull(newWritePosition)) {
            return buffer[writePosition];
        }
        return dummyElement;
    }

    @Override
    public void put() {
        writePosition.set(newWritePosition);
    }

    @Override
    public void put(T element) {
        int writePosition = this.writePosition.getPlain();
        int newWritePosition;
        if (writePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition = writePosition + 1;
        }
        if (isBufferNotFull(newWritePosition)) {
            buffer[writePosition] = element;
            this.writePosition.set(newWritePosition);
        }
    }

    private boolean isBufferNotFull(int newWritePosition) {
        if (discarding) {
            return readPosition.get() != newWritePosition;
        }
        writeBusyWaitStrategy.reset();
        while (readPosition.get() == newWritePosition) {
            writeBusyWaitStrategy.tick();
        }
        return true;
    }

    @Override
    public synchronized T take() {
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
        T element = buffer[readPosition];
        if (gcEnabled) {
            buffer[readPosition] = null;
        }
        return element;
    }

    @Override
    public synchronized void fill(Array<T> buffer) {
        int readPosition = this.readPosition.getPlain();
        int bufferSize = buffer.getCapacity();
        readBusyWaitStrategy.reset();
        while (size(readPosition) < bufferSize) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition < capacity - bufferSize) {
            int newReadPosition = readPosition + bufferSize;
            for (int j = 0; readPosition < newReadPosition; readPosition++) {
                buffer.setElement(j++, this.buffer[readPosition]);
                if (gcEnabled) {
                    this.buffer[readPosition] = null;
                }
            }
            this.readPosition.set(newReadPosition);
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
        int newReadPosition = readPosition + bufferSize - capacity;
        for (; readPosition < capacity; readPosition++) {
            buffer.setElement(j++, this.buffer[readPosition]);
            if (gcEnabled) {
                this.buffer[readPosition] = null;
            }
        }
        for (readPosition = 0; readPosition < newReadPosition; readPosition++) {
            buffer.setElement(j++, this.buffer[readPosition]);
            if (gcEnabled) {
                this.buffer[readPosition] = null;
            }
        }
        this.readPosition.set(newReadPosition);
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
                if (buffer[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }
        return containsSplit(element, readPosition, writePosition);
    }

    private boolean containsSplit(T element, int readPosition, int writePosition) {
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
