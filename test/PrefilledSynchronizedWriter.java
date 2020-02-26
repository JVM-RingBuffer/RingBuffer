package eu.menzani.ringbuffer;

class PrefilledSynchronizedWriter extends PrefilledWriter {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(PrefilledSynchronizedWriter::new, ringBuffer);
    }

    private PrefilledSynchronizedWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void tick(int i) {
        synchronized (ringBuffer) {
            super.tick(i);
        }
    }
}
