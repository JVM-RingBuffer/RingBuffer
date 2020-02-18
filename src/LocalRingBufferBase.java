package eu.menzani.ringbuffer;

abstract class LocalRingBufferBase<T> extends RingBufferBase<T> {
    int readPosition;
    int writePosition;

    LocalRingBufferBase(RingBufferOptions<?> options) {
        super(options);
    }

    @Override
    public void commit() {
    }

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
