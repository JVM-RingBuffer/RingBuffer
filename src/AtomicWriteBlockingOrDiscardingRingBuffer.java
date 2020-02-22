package eu.menzani.ringbuffer;

class AtomicWriteBlockingOrDiscardingRingBuffer<T> extends VolatileBlockingOrDiscardingRingBuffer<T> {
    AtomicWriteBlockingOrDiscardingRingBuffer(RingBufferBuilder<?> options, boolean discarding) {
        super(options, discarding);
    }

    @Override
    public synchronized void put(T element) {
        super.put(element);
    }
}
