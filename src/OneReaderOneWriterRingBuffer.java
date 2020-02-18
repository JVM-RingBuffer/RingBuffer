package eu.menzani.ringbuffer;

public class OneReaderOneWriterRingBuffer<T> extends AbstractRingBuffer<T> {
    public static <T> RingBuffer<T> blocking(RingBufferOptions<?> options) {
        return OneReaderOneWriterBlockingOrDiscardingRingBuffer.blocking(options);
    }

    public static <T> RingBuffer<T> discarding(RingBufferOptions<T> options) {
        return OneReaderOneWriterBlockingOrDiscardingRingBuffer.discarding(options);
    }

    private final BusyWaitStrategy readBusyWaitStrategy;

    private int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    public OneReaderOneWriterRingBuffer(RingBufferOptions<?> options) {
        super(options);
        readBusyWaitStrategy = options.getReadBusyWaitStrategy();
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

    // Used by ManyReadersOneWriterRingBuffer
    int size(Object monitor) {
        int writePosition = this.writePosition;
        int readPosition;
        synchronized (monitor) {
            readPosition = this.readPosition;
        }
        if (writePosition >= readPosition) {
            return writePosition - readPosition;
        }
        return capacity - (readPosition - writePosition);
    }

    // Used by ManyReadersOneWriterRingBuffer
    boolean isEmpty(Object monitor) {
        int readPosition;
        synchronized (monitor) {
            readPosition = this.readPosition;
        }
        return writePosition == readPosition;
    }
}
