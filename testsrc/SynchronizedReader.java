package eu.menzani.ringbuffer;

class SynchronizedReader extends Reader {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> new SynchronizedReader(numIterations, ringBuffer));
    }

    private SynchronizedReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(false, numIterations, ringBuffer);
    }

    @Override
    void loop() {
        for (int i = 0; i < numIterations; i++) {
            synchronized (ringBuffer) {
                sum += ringBuffer.take().getData();
            }
        }
    }
}
