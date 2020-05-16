package test;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.java.Int;

class BatchReader extends Reader {
    static long runGroupAsync(int batchSize, RingBuffer<Event> ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(new Factory() {
            @Override
            public TestThread newInstance(int numIterations) {
                return new BatchReader(numIterations, batchSize, ringBuffer);
            }
        });
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

    BatchReader(int numIterations, int batchSize, RingBuffer<Event> ringBuffer) {
        super(Int.ceilDiv(numIterations, batchSize), ringBuffer);
        this.batchSize = batchSize;
    }

    @Override
    String getProfilerName() {
        return "BatchReader";
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
