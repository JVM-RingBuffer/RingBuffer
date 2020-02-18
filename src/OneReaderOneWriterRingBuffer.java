package eu.menzani.ringbuffer;

public class OneReaderOneWriterRingBuffer<T> implements RingBuffer<T> {
    private final int capacity;
    private final int capacityMinusOne;
    private final Object[] buffer;
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final boolean gc;

    private int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    public OneReaderOneWriterRingBuffer(RingBufferOptions<?> options) {
        capacity = options.getCapacity();
        capacityMinusOne = options.getCapacityMinusOne();
        buffer = options.newBuffer();
        readBusyWaitStrategy = options.getReadBusyWaitStrategy();
        gc = options.getGC();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public T put() {
        int writePosition = this.writePosition;
        if (writePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition = writePosition + 1;
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

    /**
     * Must be called from the reader thread.
     */
    @Override
    public int size() {
        int writePosition = this.writePosition;
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    /**
     * Must be called from the reader thread.
     */
    @Override
    public boolean isEmpty() {
        return writePosition == readPosition;
    }
}
