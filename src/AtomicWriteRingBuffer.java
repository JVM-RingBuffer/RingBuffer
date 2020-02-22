package eu.menzani.ringbuffer;

class AtomicWriteRingBuffer<T> extends VolatileRingBuffer<T> {
    AtomicWriteRingBuffer(RingBufferBuilder<?> options) {
        super(options);
    }

    @Override
    public synchronized void put(T element) {
        super.put(element);
    }
}
