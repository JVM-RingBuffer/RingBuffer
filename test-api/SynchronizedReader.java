package test;

import eu.menzani.ringbuffer.RingBuffer;

class SynchronizedReader extends Reader {
    static long runGroupAsync(RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedReader(numIterations, ringBuffer));
        group.start();
        group.reportPerformance();
        return group.getReaderSum();
    }

    private SynchronizedReader(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
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
