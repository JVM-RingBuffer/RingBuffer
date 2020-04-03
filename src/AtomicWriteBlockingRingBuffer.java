package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Array;
import eu.menzani.ringbuffer.memory.BooleanArray;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

class AtomicWriteBlockingRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final boolean gcEnabled;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy[] writeBusyWaitStrategyArray;

    private int readPosition;
    private final Integer writePosition;

    private final BooleanArray writtenPositions;
    private final BooleanArray usedPositions;

    AtomicWriteBlockingRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
        gcEnabled = builder.isGCEnabled();
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        writeBusyWaitStrategyArray = builder.getWriteBusyWaitStrategyArray();
        writePosition = builder.newCursor();
        writtenPositions = builder.newFlagArray();
        usedPositions = builder.newFlagArray();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int nextKey() {
        return unsupported();
    }

    @Override
    public T next(int key) {
        return unsupported();
    }

    @Override
    public void put(int key) {
        unsupported();
    }

    private static <T> T unsupported() {
        throw new AssertionError("This should have been an advancing-supporting implementation.");
    }

    @Override
    public void put(T element) {
        int writePosition = this.writePosition.getAndDecrement() & capacityMinusOne;
        BusyWaitStrategy busyWaitStrategy = writeBusyWaitStrategyArray[writePosition];
        busyWaitStrategy.reset();
        while (!usedPositions.weakCompareFalseAndSetTrue(writePosition)) {
            busyWaitStrategy.tick();
        }
        buffer[writePosition] = element;
        writtenPositions.setTrue(writePosition);
    }

    @Override
    public T take() {
        readBusyWaitStrategy.reset();
        while (writtenPositions.isFalse(readPosition)) {
            readBusyWaitStrategy.tick();
        }
        writtenPositions.setFalsePlain(readPosition);
        T element = buffer[readPosition];
        if (gcEnabled) {
            buffer[readPosition] = null;
        }
        usedPositions.setFalse(readPosition);
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
        if (readPosition >= bufferSize) {
            int i = readPosition;
            readPosition -= bufferSize;
            for (int j = 0; i > readPosition; i--) {
                readBusyWaitStrategy.reset();
                while (writtenPositions.isFalse(i)) {
                    readBusyWaitStrategy.tick();
                }
                writtenPositions.setFalsePlain(i);
                buffer.setElement(j++, this.buffer[i]);
                if (gcEnabled) {
                    this.buffer[i] = null;
                }
                usedPositions.setFalse(i);
            }
        } else {
            fillSplit(buffer, bufferSize);
        }
    }

    private void fillSplit(Array<T> buffer, int bufferSize) {
        int i = readPosition;
        int j = 0;
        for (; i >= 0; i--) {
            readBusyWaitStrategy.reset();
            while (writtenPositions.isFalse(i)) {
                readBusyWaitStrategy.tick();
            }
            writtenPositions.setFalsePlain(i);
            buffer.setElement(j++, this.buffer[i]);
            if (gcEnabled) {
                this.buffer[i] = null;
            }
            usedPositions.setFalse(i);
        }
        readPosition += capacity - bufferSize;
        for (i = capacityMinusOne; i > readPosition; i--) {
            readBusyWaitStrategy.reset();
            while (writtenPositions.isFalse(i)) {
                readBusyWaitStrategy.tick();
            }
            writtenPositions.setFalsePlain(i);
            buffer.setElement(j++, this.buffer[i]);
            if (gcEnabled) {
                this.buffer[i] = null;
            }
            usedPositions.setFalse(i);
        }
    }

    @Override
    public void forEach(Consumer<T> action) {
        for (int i = 0; i < capacity; i++) {
            if (writtenPositions.isTrue(i)) {
                action.accept(buffer[i]);
            }
        }
    }

    @Override
    public boolean contains(T element) {
        for (int i = 0; i < capacity; i++) {
            if (writtenPositions.isTrue(i) && buffer[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        int usedCount = 0;
        for (int i = 0; i < capacity; i++) {
            if (usedPositions.isTrue(i)) {
                usedCount++;
            }
        }
        return usedCount;
    }

    @Override
    public boolean isEmpty() {
        return usedPositions.isFalse(readPosition);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(16);
        builder.append('[');
        for (int i = 0; i < capacity; i++) {
            if (writtenPositions.isTrue(i)) {
                builder.append(buffer[i].toString());
                builder.append(", ");
            }
        }
        int length = builder.length();
        if (length != 1) {
            builder.setLength(length - 2);
        }
        builder.append(']');
        return builder.toString();
    }
}
