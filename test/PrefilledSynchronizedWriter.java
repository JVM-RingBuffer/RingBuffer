package eu.menzani.ringbuffer;

class PrefilledSynchronizedWriter extends PrefilledWriter {
    PrefilledSynchronizedWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void tick(int i) {
        synchronized (ringBuffer) {
            super.tick(i);
        }
    }
}
