package test;

import eu.menzani.ringbuffer.RingBuffer;

class SynchronizedBatchReader extends BatchReader {
    static long runGroupAsync(int batchSize, RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new SynchronizedBatchReader(numIterations, batchSize, ringBuffer));
        group.start();
        group.reportPerformance();
        return group.getReaderSum();
    }

    private SynchronizedBatchReader(int numIterations, int batchSize, RingBuffer<Event> ringBuffer) {
        super(numIterations, batchSize, ringBuffer);
    }

    @Override
    long collect() {
        int numIterations = getNumIterations();
        RingBuffer<Event> ringBuffer = getRingBuffer();
        int batchSize = getBatchSize();
        long sum = 0L;
        for (int i = 0; i < numIterations; i++) {
            synchronized (ringBuffer) {
                ringBuffer.prepareBatch(batchSize);
                for (int j = batchSize; j > 0; j--) {
                    sum += ringBuffer.takePlain().getData();
                }
            }
        }
        return sum;
    }
}
