package eu.menzani.ringbuffer;

class SynchronizedReader extends Reader {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(SynchronizedReader::new, ringBuffer);
    }

    private SynchronizedReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void tick(int i) {
        synchronized (ringBuffer) {
            super.tick(i);
        }
    }
}
