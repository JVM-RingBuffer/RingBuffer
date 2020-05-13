package test;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Int;

class BatchReader extends Reader {
    static long runGroupAsync(int batchSize, RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BatchReader(numIterations, batchSize, ringBuffer));
        group.start();
        group.reportPerformance();
        return group.getReaderSum();
    }

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

    @Override
    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        int batchSize = this.batchSize;
        long sum = 0L;
        for (; numIterations > 0; numIterations--) {
            ringBuffer.takeBatch(batchSize);
            for (int j = batchSize; j > 0; j--) {
                sum += ringBuffer.takePlain().getData();
            }
            ringBuffer.advanceBatch();
        }
        return sum;
    }
}
