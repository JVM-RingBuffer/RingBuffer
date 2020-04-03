package perftest;

import eu.menzani.ringbuffer.RingBuffer;

class AdvancingReader extends Reader {
    static AdvancingReader runAsync(int numIterations, RingBuffer<Event> ringBuffer) {
        AdvancingReader thread = new AdvancingReader(numIterations, ringBuffer);
        thread.start();
        return thread;
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
