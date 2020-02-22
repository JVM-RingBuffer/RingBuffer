package eu.menzani.ringbuffer;

class OneReaderManyWritersBlockingOrDiscardingRingBuffer<T> extends OneReaderOneWriterBlockingOrDiscardingRingBuffer<T> {
    OneReaderManyWritersBlockingOrDiscardingRingBuffer(RingBufferBuilder<?> options, boolean discarding) {
        super(options, discarding);
    }

    @Override
    public synchronized void put(T element) {
        super.put(element);
    }
}
