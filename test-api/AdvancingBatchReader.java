package test;

import eu.menzani.ringbuffer.RingBuffer;

class AdvancingBatchReader extends BatchReader {
    static long runAsync(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        AdvancingBatchReader reader = new AdvancingBatchReader(numIterations, readBufferSize, ringBuffer);
        reader.start();
        reader.reportPerformance();
        return reader.getSum();
    }

    private AdvancingBatchReader(int numIterations, int readBufferSize, RingBuffer<Event> ringBuffer) {
        super(numIterations, readBufferSize, ringBuffer);
    }

    @Override
    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        Event[] buffer = getReadBuffer();
        long sum = 0L;
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.fill(buffer);
            for (Event event : buffer) {
                sum += event.getData();
            }
            ringBuffer.advanceBatch();
        }
        return sum;
    }
}
