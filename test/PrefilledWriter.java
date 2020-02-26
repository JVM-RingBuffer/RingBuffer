package eu.menzani.ringbuffer;

class PrefilledWriter extends TestThread {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(PrefilledWriter::new, ringBuffer);
    }

    PrefilledWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void tick(int i) {
        Event event = ringBuffer.next();
        event.setData(i);
        ringBuffer.put();
    }
}
