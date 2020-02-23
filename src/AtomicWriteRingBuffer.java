package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.StringJoiner;

class AtomicWriteRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;
    private final boolean gcEnabled;
    private final BusyWaitStrategy readBusyWaitStrategy;

    private int readPosition;
    private final LazyAtomicInteger writePosition = new LazyAtomicInteger();

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
        return (T) buffer[incrementWritePosition()];
    }

    @Override
    public void put() {
        writePosition.afterUpdate();
    }

    @Override
    public void put(T element) {
        buffer[incrementWritePosition()] = element;
        writePosition.afterUpdate();
    }

    private int incrementWritePosition() {
        return IntPair.getFirst(writePosition.update(capacityMinusOne, (writePosition, capacityMinusOne) -> {
            if (writePosition == capacityMinusOne) {
                return 0;
            }
            return writePosition + 1;
        }));
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
        return this.writePosition.get() == readPosition;
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
