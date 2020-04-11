package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class AdvancingReader extends Reader {
    static long runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        AdvancingReader reader = new AdvancingReader(numIterations, ringBuffer);
        reader.start();
        reader.reportPerformance();
        return reader.getSum();
    }

    private AdvancingReader(int numIterations, RingBuffer<Event> ringBuffer) {
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
