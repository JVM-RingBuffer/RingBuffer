package eu.menzani.ringbuffer;

class Reader extends TestThread {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> new Reader(false, numIterations, ringBuffer));
    }

    static Reader newReader(int numIterations, RingBuffer<Event> ringBuffer) {
        return new Reader(true, numIterations, ringBuffer);
    }

    long sum;

    Reader(boolean bindToCPU, int numIterations, RingBuffer<Event> ringBuffer) {
        super(bindToCPU, numIterations, ringBuffer);
    }

    long getSum() {
        return sum;
    }

    @Override
    void loop() {
        for (int i = 0; i < numIterations; i++) {
            sum += ringBuffer.take().getData();
        }
    }

    @Override
    void waitForCompletion() {
        try {
            join();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }
}
