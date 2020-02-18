package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

class OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> extends AbstractRingBuffer<T> {
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final boolean blocking;
    private final BusyWaitStrategy writeBusyWaitStrategy;
    private final T dummyElement;

    private volatile int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    static <T> OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> blocking(RingBufferOptions<?> options) {
        return new OneReaderOneWriterBlockingOrDiscardingRingBuffer<>(options, true);
    }

    static <T> OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> discarding(RingBufferOptions<T> options) {
        return new OneReaderOneWriterBlockingOrDiscardingRingBuffer<>(options, false);
    }

    private OneReaderOneWriterBlockingOrDiscardingRingBuffer(RingBufferOptions<?> options, boolean blocking) {
        super(options);
        readBusyWaitStrategy = options.getReadBusyWaitStrategy();
        this.blocking = blocking;
        if (blocking) {
            writeBusyWaitStrategy = options.getWriteBusyWaitStrategy();
            dummyElement = null;
        } else {
            writeBusyWaitStrategy = null;
            dummyElement = ((RingBufferOptions<T>) options).getDummyElement();
        }
    }

    @Override
    public T put() {
        int writePosition = this.writePosition;
        if (writePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition = writePosition + 1;
        }
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
        int newWritePosition = writePosition;
        if (newWritePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition++;
        }
        if (blocking) {
            writeBusyWaitStrategy.reset();
            while (readPosition == newWritePosition) {
                writeBusyWaitStrategy.tick();
            }
        } else if (readPosition != newWritePosition) {
            buffer[writePosition] = element;
            writePosition = newWritePosition;
        }
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
    public int size() {
        int writePosition = this.writePosition;
        int readPosition = this.readPosition;
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    @Override
    public boolean isEmpty() {
        return writePosition == readPosition;
    }
}
