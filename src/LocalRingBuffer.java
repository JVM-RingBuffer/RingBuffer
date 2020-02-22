package eu.menzani.ringbuffer;

class LocalRingBuffer<T> extends LocalRingBufferBase<T> {
    LocalRingBuffer(RingBufferBuilder<?> builder) {
        super(builder);
    }

    @Override
    public T put() {
        Object element = buffer[writePosition];
        incrementWritePosition();
        return (T) element;
    }

    @Override
    public void put(T element) {
        buffer[writePosition] = element;
        incrementWritePosition();
    }
}
