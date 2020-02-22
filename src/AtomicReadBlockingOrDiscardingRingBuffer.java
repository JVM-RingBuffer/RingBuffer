package eu.menzani.ringbuffer;

class AtomicReadBlockingOrDiscardingRingBuffer<T> extends VolatileBlockingOrDiscardingRingBuffer<T> {
    AtomicReadBlockingOrDiscardingRingBuffer(RingBufferBuilder<?> options, boolean discarding) {
        super(options, discarding);
    }

    @Override
    public synchronized T take() {
        return super.take();
    }
}
