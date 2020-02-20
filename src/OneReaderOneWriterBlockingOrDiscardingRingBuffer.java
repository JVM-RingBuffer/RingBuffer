package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

class OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> extends RingBufferBase<T> {
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final boolean blocking;
    private final BusyWaitStrategy writeBusyWaitStrategy;
    private final T dummyElement;

    private volatile int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    static <T> OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> blocking(RingBufferBuilder<?> options) {
        return new OneReaderOneWriterBlockingOrDiscardingRingBuffer<>(options, true);
    }

    static <T> OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> discarding(RingBufferBuilder<T> options) {
        return new OneReaderOneWriterBlockingOrDiscardingRingBuffer<>(options, false);
    }

    private OneReaderOneWriterBlockingOrDiscardingRingBuffer(RingBufferBuilder<?> options, boolean blocking) {
        super(options);
        readBusyWaitStrategy = options.getReadBusyWaitStrategy();
        this.blocking = blocking;
        writeBusyWaitStrategy = options.getWriteBusyWaitStrategy();
        if (blocking) {
            dummyElement = null;
        } else {
            dummyElement = ((RingBufferBuilder<T>) options).getDummyElement();
        }
    }

    @Override
    public T put() {
        int writePosition = this.writePosition;
        newWritePosition = incrementWritePosition(writePosition);
        if (blocking) {
            writeBusyWaitStrategy.reset();
            while (readPosition == newWritePosition) {
                writeBusyWaitStrategy.tick();
            }
        } else if (readPosition == newWritePosition) {
            return dummyElement;
        }
        return (T) buffer[writePosition];
    }

    @Override
    public void commit() {
        writePosition = newWritePosition;
    }

    @Override
    public void put(T element) {
        int writePosition = this.writePosition;
        int newWritePosition = incrementWritePosition(writePosition);
        if (blocking) {
            writeBusyWaitStrategy.reset();
            while (readPosition == newWritePosition) {
                writeBusyWaitStrategy.tick();
            }
        } else if (readPosition == newWritePosition) {
            return;
        }
        buffer[writePosition] = element;
        this.writePosition = newWritePosition;
    }

    @Override
    public T take() {
        int readPosition = this.readPosition;
        readBusyWaitStrategy.reset();
        while (writePosition == readPosition) {
            readBusyWaitStrategy.tick();
        }
        if (readPosition == capacityMinusOne) {
            this.readPosition = 0;
        } else {
            this.readPosition = readPosition + 1;
        }
        Object element = buffer[readPosition];
        if (gc) {
            buffer[readPosition] = null;
        }
        return (T) element;
    }

    @Override
    int getReadPosition() {
        return readPosition;
    }

    @Override
    int getWritePosition() {
        return writePosition;
    }
}
