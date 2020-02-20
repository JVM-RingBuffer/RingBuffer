package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

class OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> extends RingBufferBase<T> {
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final boolean discarding;
    private final BusyWaitStrategy writeBusyWaitStrategy;
    private final T dummyElement;

    private final LazyVolatileInteger readPosition = new LazyVolatileInteger();
    private final LazyVolatileInteger writePosition = new LazyVolatileInteger();

    private int newWritePosition;

    static <T> OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> blocking(RingBufferBuilder<?> options) {
        return new OneReaderOneWriterBlockingOrDiscardingRingBuffer<>(options, false);
    }

    static <T> OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> discarding(RingBufferBuilder<T> options) {
        return new OneReaderOneWriterBlockingOrDiscardingRingBuffer<>(options, true);
    }

    private OneReaderOneWriterBlockingOrDiscardingRingBuffer(RingBufferBuilder<?> options, boolean discarding) {
        super(options);
        readBusyWaitStrategy = options.getReadBusyWaitStrategy();
        this.discarding = discarding;
        writeBusyWaitStrategy = options.getWriteBusyWaitStrategy();
        if (discarding) {
            dummyElement = ((RingBufferBuilder<T>) options).getDummyElement();
        } else {
            dummyElement = null;
        }
    }

    @Override
    public T put() {
        int writePosition = this.writePosition.getFromSameThread();
        newWritePosition = incrementWritePosition(writePosition);
        if (waitForRead(newWritePosition)) {
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
        if (waitForRead(newWritePosition)) {
            buffer[writePosition] = element;
            this.writePosition.set(newWritePosition);
        }
    }

    private boolean waitForRead(int newWritePosition) {
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
