package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class Reader extends TestThread {
    static TestThreadGroup newGroup(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> new Reader(numIterations, ringBuffer));
    }

    static Reader newReader(int numIterations, RingBuffer<Event> ringBuffer) {
        return new Reader(numIterations, ringBuffer);
    }

    long sum;

    Reader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    long getSum() {
        return sum;
    }

    @Override
    void loop() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        long sum = 0L;
        for (int i = 0; i < numIterations; i++) {
            sum += ringBuffer.take().getData();
        }
        this.sum = sum;
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
