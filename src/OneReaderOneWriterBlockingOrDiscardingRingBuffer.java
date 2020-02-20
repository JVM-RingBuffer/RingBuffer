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

    static <T> OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> blocking(RingBufferBuilder options) {
        return new OneReaderOneWriterBlockingOrDiscardingRingBuffer<>(options, true);
    }

    static <T> OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> discarding(RingBufferBuilder options) {
        return new OneReaderOneWriterBlockingOrDiscardingRingBuffer<>(options, false);
    }

    private OneReaderOneWriterBlockingOrDiscardingRingBuffer(RingBufferBuilder options, boolean blocking) {
        super(options);
        readBusyWaitStrategy = options.getReadBusyWaitStrategy();
        this.blocking = blocking;
        if (blocking) {
            writeBusyWaitStrategy = options.getWriteBusyWaitStrategy();
            dummyElement = null;
        } else {
            writeBusyWaitStrategy = null;
            dummyElement = (T) options.getDummyElement();
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
        writePosition = newWritePosition;
    }

    @Override
    public T take() {
        int oldReadPosition = readPosition;
        readBusyWaitStrategy.reset();
        while (writePosition == oldReadPosition) {
            readBusyWaitStrategy.tick();
        }
        if (oldReadPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition = oldReadPosition + 1;
        }
        Object element = buffer[oldReadPosition];
        if (gc) {
            buffer[oldReadPosition] = null;
        }
        return (T) element;
    }

    @Override
    public boolean contains(T element) {
        return contains(readPosition, writePosition, element);
    }

    @Override
    public int size() {
        return size(readPosition, writePosition);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(readPosition, writePosition);
    }

    @Override
    public String toString() {
        return toString(readPosition, writePosition);
    }
}
