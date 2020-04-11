package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.memory.BooleanArray;
import eu.menzani.ringbuffer.memory.Integer;
import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.function.Consumer;

import static eu.menzani.ringbuffer.RingBufferHelper.*;

class AtomicWriteBlockingPrefilledRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final T[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final BusyWaitStrategy[] writeBusyWaitStrategyArray;

    private int readPosition;
    private final Integer writePosition;

    private int newReadPosition;

    private final BooleanArray writtenPositions;
    private final BooleanArray usedPositions;

    AtomicWriteBlockingPrefilledRingBuffer(RingBufferBuilder<T> builder) {
        capacity = builder.getCapacity();
        capacityMinusOne = builder.getCapacityMinusOne();
        buffer = builder.getBuffer();
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
    public T next() {
        return keyRequired();
    }

    @Override
    public void put() {
        keyRequired();
    }

    @Override
    public int nextKey() {
        return writePosition.getAndDecrement() & capacityMinusOne;
    }

    @Override
    public T next(int key) {
        BusyWaitStrategy busyWaitStrategy = writeBusyWaitStrategyArray[key];
        busyWaitStrategy.reset();
        while (!usedPositions.weakCompareFalseAndSetTrue(key)) {
            busyWaitStrategy.tick();
        }
        return buffer[key];
    }

    @Override
    public void put(int key) {
        writtenPositions.setTrue(key);
    }

    @Override
    public void put(T element) {
        shouldNotBeAdvancing();
    }

    @Override
    public T take() {
        readBusyWaitStrategy.reset();
        while (writtenPositions.isFalse(readPosition)) {
            readBusyWaitStrategy.tick();
        }
        writtenPositions.setFalsePlain(readPosition);
        if (readPosition == 0) {
            newReadPosition = capacityMinusOne;
        } else {
            newReadPosition = readPosition - 1;
        }
        return buffer[readPosition];
    }

    @Override
    public void fill(T[] buffer) {
        if (readPosition >= buffer.length) {
            int i = readPosition;
            newReadPosition = i - buffer.length;
            for (int j = 0; i > newReadPosition; i--) {
                readBusyWaitStrategy.reset();
                while (writtenPositions.isFalse(i)) {
                    readBusyWaitStrategy.tick();
                }
                writtenPositions.setFalsePlain(i);
                buffer[j++] = this.buffer[i];
            }
        } else {
            fillSplit(buffer);
        }
    }

    private void fillSplit(T[] buffer) {
        int i = readPosition;
        newReadPosition = i + capacity - buffer.length;
        int j = 0;
        for (; i >= 0; i--) {
            readBusyWaitStrategy.reset();
            while (writtenPositions.isFalse(i)) {
                readBusyWaitStrategy.tick();
            }
            writtenPositions.setFalsePlain(i);
            buffer[j++] = this.buffer[i];
        }
        for (i = capacityMinusOne; i > newReadPosition; i--) {
            readBusyWaitStrategy.reset();
            while (writtenPositions.isFalse(i)) {
                readBusyWaitStrategy.tick();
            }
            writtenPositions.setFalsePlain(i);
            buffer[j++] = this.buffer[i];
        }
    }

    @Override
    public void advance() {
        usedPositions.setFalse(readPosition);
        readPosition = newReadPosition;
    }

    @Override
    public void advanceBatch() {
        if (newReadPosition < readPosition) {
            for (int i = newReadPosition + 1; i < readPosition; i++) {
                usedPositions.setFalsePlain(i);
            }
        } else {
            advanceBatchSplit();
        }
        usedPositions.setFalse(readPosition);
        readPosition = newReadPosition;
    }

    private void advanceBatchSplit() {
        int i = newReadPosition + 1;
        for (; i < capacity; i++) {
            usedPositions.setFalsePlain(i);
        }
        for (i = 0; i < readPosition; i++) {
            usedPositions.setFalsePlain(i);
        }
    }

    @Override
    public void forEach(Consumer<T> action) {
        for (int i = readPosition; writtenPositions.isTrue(i); ) {
            action.accept(buffer[i]);
            if (i == 0) {
                i = capacityMinusOne;
            } else {
                i--;
            }
            if (i == readPosition) {
                break;
            }
        }
    }

    @Override
    public boolean contains(T element) {
        for (int i = readPosition; writtenPositions.isTrue(i); ) {
            if (buffer[i].equals(element)) {
                return true;
            }
            if (i == 0) {
                i = capacityMinusOne;
            } else {
                i--;
            }
            if (i == readPosition) {
                break;
            }
        }
        return false;
    }

    @Override
    public int size() {
        int usedCount = 0;
        for (int i = readPosition; usedPositions.isTrue(i); ) {
            usedCount++;
            if (i == 0) {
                i = capacityMinusOne;
            } else {
                i--;
            }
            if (i == readPosition) {
                break;
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
        for (int i = readPosition; writtenPositions.isTrue(i); ) {
            builder.append(buffer[i].toString());
            builder.append(", ");
            if (i == 0) {
                i = capacityMinusOne;
            } else {
                i--;
            }
            if (i == readPosition) {
                break;
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
