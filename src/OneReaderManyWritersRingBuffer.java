package eu.menzani.ringbuffer;

class OneReaderManyWritersRingBuffer<T> extends OneReaderOneWriterRingBuffer<T> {
    OneReaderManyWritersRingBuffer(RingBufferBuilder<?> options) {
        super(options);
    }

    @Override
    public synchronized void put(T element) {
        super.put(element);
    }
}
