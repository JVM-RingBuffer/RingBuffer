package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.wait.BusyWaitStrategy;

class OneReaderOneWriterRingBuffer<T> extends RingBufferBase<T> {
    private final BusyWaitStrategy readBusyWaitStrategy;

    private int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    OneReaderOneWriterRingBuffer(RingBufferBuilder options) {
        super(options);
        readBusyWaitStrategy = options.getReadBusyWaitStrategy();
    }

    @Override
    public T put() {
        int writePosition = this.writePosition;
        newWritePosition = incrementWritePosition(writePosition);
        return (T) buffer[writePosition];
    }

    @Override
    public void commit() {
        writePosition = newWritePosition;
    }

    @Override
    public void put(T element) {
        int newWritePosition = incrementWritePosition(writePosition);
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
            readPosition++;
        }
        Object element = buffer[oldReadPosition];
        if (gc) {
            buffer[oldReadPosition] = null;
        }
        return (T) element;
    }

    // Must be called from the reader thread
    @Override
    public boolean contains(T element) {
        return contains(readPosition, writePosition, element);
    }

    // Must be called from the reader thread
    @Override
    public int size() {
        return size(readPosition, writePosition);
    }

    // Must be called from the reader thread
    @Override
    public boolean isEmpty() {
        return isEmpty(readPosition, writePosition);
    }

    // Must be called from the reader thread
    @Override
    public String toString() {
        return toString(readPosition, writePosition);
    }

    // Needed by ManyReadersOneWriterRingBuffer
    int getReadPosition() {
        return readPosition;
    }
    int getWritePosition() {
        return writePosition;
    }
}
