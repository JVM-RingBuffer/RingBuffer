package eu.menzani.ringbuffer;

class AtomicReadRingBuffer<T> extends VolatileRingBuffer<T> {
    AtomicReadRingBuffer(RingBufferBuilder<?> options) {
        super(options);
    }

    @Override
    public synchronized T take() {
        return super.take();
    }

    @Override
    synchronized int getReadPosition() {
        return super.getReadPosition();
    }
}
