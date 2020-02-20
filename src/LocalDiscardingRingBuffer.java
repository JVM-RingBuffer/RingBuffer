package eu.menzani.ringbuffer;

class LocalDiscardingRingBuffer<T> extends LocalRingBufferBase<T> {
    private final T dummyElement;

    LocalDiscardingRingBuffer(RingBufferBuilder<T> options) {
        super(options);
        dummyElement = options.getDummyElement();
    }

    @Override
    public T put() {
        int oldWritePosition = writePosition;
        incrementWritePosition();
        if (readPosition == writePosition) {
            return dummyElement;
        }
        return (T) buffer[oldWritePosition];
    }

    @Override
    public void put(T element) {
        int oldWritePosition = writePosition;
        incrementWritePosition();
        if (readPosition != writePosition) {
            buffer[oldWritePosition] = element;
        }
    }
}
