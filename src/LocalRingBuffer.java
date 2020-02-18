package eu.menzani.ringbuffer;

public class LocalRingBuffer<T> extends LocalRingBufferBase<T> {
    public LocalRingBuffer(RingBufferOptions<?> options) {
        super(options);
    }

    @Override
    public T put() {
        Object element = buffer[writePosition];
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
        return (T) element;
    }

    @Override
    public void put(T element) {
        buffer[writePosition] = element;
        if (writePosition == capacityMinusOne) {
            writePosition = 0;
        } else {
            writePosition++;
        }
    }
}
