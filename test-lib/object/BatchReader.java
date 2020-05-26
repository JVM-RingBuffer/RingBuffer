package test.object;

import eu.menzani.ringbuffer.java.Number;
import eu.menzani.ringbuffer.object.RingBuffer;
import test.TestThreadGroup;

class BatchReader extends Reader {
    static long runGroupAsync(int batchSize, RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BatchReader(numIterations, batchSize, ringBuffer));
        group.start();
        group.waitForCompletion();
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, int batchSize, RingBuffer<Event> ringBuffer) {
        BatchReader reader = new BatchReader(numIterations, batchSize, ringBuffer);
        reader.startNow();
        reader.waitForCompletion();
        return reader.getSum();
    }

    private final int batchSize;

    private BatchReader(int numIterations, int batchSize, RingBuffer<Event> ringBuffer) {
        super(Number.ceilDiv(numIterations, batchSize), ringBuffer);
        this.batchSize = batchSize;
    }

    @Override
    long collect() {
        RingBuffer<Event> ringBuffer = getRingBuffer();
        int batchSize = this.batchSize;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            ringBuffer.takeBatch(batchSize);
            for (int j = batchSize; j > 0; j--) {
                sum += ringBuffer.takePlain().getData();
            }
            ringBuffer.advanceBatch();
        }
        return sum;
    }
}
