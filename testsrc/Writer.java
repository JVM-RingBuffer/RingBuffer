package eu.menzani.ringbuffer;

class Writer extends TestThread {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup((numIterations) -> new Writer(false, numIterations, ringBuffer));
    }

    static Writer newWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        return new Writer(true, numIterations, ringBuffer);
    }

    private Writer(boolean bind, int numIterations, RingBuffer<Event> ringBuffer) {
        super(bind, numIterations, ringBuffer);
    }

    @Override
    void loop() {
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.put(new Event(i));
        }
    }
}
