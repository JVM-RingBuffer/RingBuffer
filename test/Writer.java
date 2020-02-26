package eu.menzani.ringbuffer;

class Writer extends TestThread {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(Writer::new, ringBuffer);
    }

    Writer(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    void loop() {
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.put(new Event(i));
        }
    }
}
