package test;

import eu.menzani.ringbuffer.RingBuffer;

class Reader extends TestThread {
    static long runGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new Reader(numIterations, ringBuffer));
        group.start();
        group.reportPerformance();
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        Reader reader = new Reader(numIterations, ringBuffer);
        reader.start();
        reader.reportPerformance();
        return reader.getSum();
    }

    static long runSync(int numIterations, RingBuffer<Event> ringBuffer) {
        Reader reader = new Reader(numIterations, ringBuffer);
        reader.run();
        reader.reportPerformance();
        return reader.getSum();
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
            ringBuffer.advance();
        }
        return sum;
    }
}
