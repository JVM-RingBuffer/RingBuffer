package test;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Int;

class BatchReader extends Reader {
    static long runAsync(int numIterations, int batchSize, RingBuffer<Event> ringBuffer) {
        BatchReader reader = new BatchReader(numIterations, batchSize, ringBuffer);
        reader.start();
        reader.reportPerformance();
        return reader.getSum();
    }

    static long runSync(int numIterations, int batchSize, RingBuffer<Event> ringBuffer) {
        BatchReader reader = new BatchReader(numIterations, batchSize, ringBuffer);
        reader.run();
        reader.reportPerformance();
        return reader.getSum();
    }

    private final int batchSize;

    BatchReader(int numIterations, int batchSize, RingBuffer<Event> ringBuffer) {
        super(Int.ceilDiv(numIterations, batchSize), ringBuffer);
        this.batchSize = batchSize;
    }

    int getBatchSize() {
        return batchSize;
    }

    @Override
    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        int batchSize = this.batchSize;
        long sum = 0L;
        for (int i = 0; i < numIterations; i++) {
            ringBuffer.prepareTake(batchSize);
            for (int j = batchSize; j > 0; j--) {
                sum += ringBuffer.takeNow().getData();
            }
        }
        return sum;
    }
}
