package eu.menzani.ringbuffer;

class LocalDiscardingRingBuffer<T> extends LocalRingBufferBase<T> {
    private final T dummyElement;

    LocalDiscardingRingBuffer(RingBufferBuilder options) {
        super(options);
        dummyElement = (T) options.getDummyElement();
    }

    @Override
    public T put() {
        int oldWritePosition = incrementWritePosition();
        if (readPosition == writePosition) {
            return dummyElement;
        }
        return (T) buffer[oldWritePosition];
    }

    @Override
    public void put(T element) {
        int oldWritePosition = incrementWritePosition();
        if (readPosition != writePosition) {
            buffer[oldWritePosition] = element;
        }
    }

    private int incrementWritePosition() {
        int oldWritePosition = writePosition;
        if (oldWritePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
        return oldWritePosition;
    }
}
