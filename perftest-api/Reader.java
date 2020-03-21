package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class Reader extends TestThread {
    static TestThreadGroup runGroupAsync(RingBuffer<Event> ringBuffer) {
        return new TestThreadGroup(numIterations -> runAsync(numIterations, ringBuffer));
    }

    static Reader runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        Reader thread = new Reader(numIterations, ringBuffer);
        thread.start();
        return thread;
    }

    static Reader runSync(int numIterations, RingBuffer<Event> ringBuffer) {
        Reader thread = new Reader(numIterations, ringBuffer);
        thread.run();
        return thread;
    }

    private long sum;

    Reader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    long getSum() {
        return sum;
    }

    @Override
    void loop() {
        sum = collect();
    }

    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        long sum = 0L;
        for (int i = 0; i < numIterations; i++) {
            sum += ringBuffer.take().getData();
        }
        return sum;
    }

    @Override
    void waitForCompletion() {
        try {
            join();
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }
}
