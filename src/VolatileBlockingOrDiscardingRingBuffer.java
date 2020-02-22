package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

class VolatileBlockingOrDiscardingRingBuffer<T> extends RingBufferBase<T> {
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final boolean discarding;
    private final BusyWaitStrategy writeBusyWaitStrategy;
    private final T dummyElement;

    private final LazyVolatileInteger readPosition = new LazyVolatileInteger();
    private final LazyVolatileInteger writePosition = new LazyVolatileInteger();

    private int newWritePosition;

    static <T> VolatileBlockingOrDiscardingRingBuffer<T> blocking(RingBufferBuilder<?> builder) {
        return new VolatileBlockingOrDiscardingRingBuffer<>(builder, false);
    }

    static <T> VolatileBlockingOrDiscardingRingBuffer<T> discarding(RingBufferBuilder<?> builder) {
        return new VolatileBlockingOrDiscardingRingBuffer<>(builder, true);
    }

    private VolatileBlockingOrDiscardingRingBuffer(RingBufferBuilder<?> builder, boolean discarding) {
        super(builder);
        readBusyWaitStrategy = builder.getReadBusyWaitStrategy();
        this.discarding = discarding;
        writeBusyWaitStrategy = builder.getWriteBusyWaitStrategy();
        if (discarding) {
            dummyElement = ((RingBufferBuilder<T>) builder).getDummyElement();
        } else {
            dummyElement = null;
        }
    }

    @Override
    public T put() {
        int writePosition = this.writePosition.getFromSameThread();
        newWritePosition = incrementWritePosition(writePosition);
        if (isBufferNotFull(newWritePosition)) {
            return (T) buffer[writePosition];
        }
        return dummyElement;
    }

    @Override
    public void commit() {
        writePosition.set(newWritePosition);
    }

    @Override
    public void put(T element) {
        int writePosition = this.writePosition.getFromSameThread();
        int newWritePosition = incrementWritePosition(writePosition);
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
        if (gc) {
            buffer[readPosition] = null;
        }
        return (T) element;
    }

    @Override
    int getReadPosition() {
        return readPosition.get();
    }

    @Override
    int getWritePosition() {
        return writePosition.get();
    }
}
