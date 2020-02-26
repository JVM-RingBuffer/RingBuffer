package eu.menzani.ringbuffer;

class PrefilledSynchronizedWriter extends TestThread {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(PrefilledSynchronizedWriter::new, ringBuffer);
    }

    private PrefilledSynchronizedWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        for (int i = 0; i < numIterations; i++) {
            synchronized (ringBuffer) {
                Event event = ringBuffer.next();
                event.setData(i);
                ringBuffer.put();
            }
        }
    }
}
