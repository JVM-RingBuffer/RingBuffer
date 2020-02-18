package eu.menzani.ringbuffer;

public class OneReaderOneWriterDiscardingRingBuffer<T> extends AbstractRingBuffer<T> {
    private final BusyWaitStrategy readBusyWaitStrategy;
    private final T dummyElement;

    private volatile int readPosition;
    private volatile int writePosition;

    private int newWritePosition;

    public OneReaderOneWriterDiscardingRingBuffer(RingBufferOptions<T> options) {
        super(options);
        readBusyWaitStrategy = options.getReadBusyWaitStrategy();
        dummyElement = options.getDummyElement();
    }

    @Override
    public T put() {
        int writePosition = this.writePosition;
        if (writePosition == capacityMinusOne) {
            newWritePosition = 0;
        } else {
            newWritePosition = writePosition + 1;
        }
        if (readPosition == newWritePosition) {
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
        if (readPosition != newWritePosition) {
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
