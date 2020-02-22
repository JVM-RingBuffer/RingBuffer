package eu.menzani.ringbuffer;

class ManyReadersOneWriterBlockingOrDiscardingRingBuffer<T> extends OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> {
    ManyReadersOneWriterBlockingOrDiscardingRingBuffer(RingBufferBuilder<?> options, boolean discarding) {
        super(options, discarding);
    }

    @Override
    public synchronized T take() {
        return super.take();
    }
}
