package eu.menzani.ringbuffer;

class Writer extends TestThread {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup((numIterations) -> new Writer(numIterations, ringBuffer));
    }

    static Writer newWriter(int numIterations, RingBuffer<Event> ringBuffer) {
        return new Writer(numIterations, ringBuffer);
    }

    private Writer(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.put(new Event(i));
        }
    }
}
