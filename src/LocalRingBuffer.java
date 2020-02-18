package eu.menzani.ringbuffer;

public class LocalRingBuffer<T> extends LocalRingBufferBase<T> {
    public LocalRingBuffer(RingBufferOptions<?> options) {
        super(options);
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

    private void incrementWritePosition() {
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
    }
}
