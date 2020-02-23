package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

import java.util.StringJoiner;

class AtomicWriteBlockingOrDiscardingRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;
    private final boolean gcEnabled;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final boolean discarding;
    private final BusyWaitStrategy writeBusyWaitStrategy;
    private final T dummyElement;

    private final LazyVolatileInteger readPosition = new LazyVolatileInteger();
    private final LazyAtomicInteger writePosition = new LazyAtomicInteger();

    AtomicWriteBlockingOrDiscardingRingBuffer(RingBufferBuilder<T> builder, boolean discarding) {
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
        long intPair = incrementWritePosition();
        if (isBufferNotFull(IntPair.getSecond(intPair))) {
            return (T) buffer[IntPair.getFirst(intPair)];
        }
        return dummyElement;
    }

    @Override
    public void put() {
        writePosition.afterUpdate();
    }

    @Override
    public void put(T element) {
        long intPair = incrementWritePosition();
        if (isBufferNotFull(IntPair.getSecond(intPair))) {
            buffer[IntPair.getFirst(intPair)] = element;
            writePosition.afterUpdate();
        }
    }

    private long incrementWritePosition() {
        return writePosition.update(capacityMinusOne, (writePosition, capacityMinusOne) -> {
            if (writePosition == capacityMinusOne) {
                return 0;
            }
            return writePosition + 1;
        });
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
    public T take() {
        int readPosition = this.readPosition.getFromSameThread();
        readBusyWaitStrategy.reset();
        while (writePosition.get() == readPosition) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == capacityMinusOne) {
            this.readPosition.set(0);
        } else {
            this.readPosition.set(readPosition + 1);
        }
        Object element = buffer[readPosition];
        if (gcEnabled) {
            buffer[readPosition] = null;
        }
        return (T) element;
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
        int readPosition = this.readPosition.get();
        int writePosition = this.writePosition.get();
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
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
