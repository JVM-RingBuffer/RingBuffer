package eu.menzani.ringbuffer;

class ManyReadersOneWriterRingBuffer<T> extends OneReaderOneWriterRingBuffer<T> {
    ManyReadersOneWriterRingBuffer(RingBufferBuilder<?> options) {
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
