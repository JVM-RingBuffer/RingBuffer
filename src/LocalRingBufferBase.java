package eu.menzani.ringbuffer;

abstract class LocalRingBufferBase<T> extends RingBufferBase<T> {
    int readPosition;
    int writePosition;

    LocalRingBufferBase(RingBufferBuilder<?> options) {
        super(options);
    }

    @Override
    public void commit() {}

    @Override
    public T take() {
        if (writePosition == readPosition) {
            return null;
        }
        Object element = buffer[readPosition];
        if (gc) {
            buffer[readPosition] = null;
        }
        if (readPosition == capacityMinusOne) {
            readPosition = 0;
        } else {
            readPosition++;
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

    void incrementWritePosition() {
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
    }
}
