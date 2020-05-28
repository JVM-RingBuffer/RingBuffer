package test.object;

import org.ringbuffer.java.Number;
import org.ringbuffer.object.RingBuffer;
import test.Profiler;
import test.TestThreadGroup;

class BatchReader extends Reader {
    static long runGroupAsync(int batchSize, RingBuffer<Event> ringBuffer, Profiler profiler) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BatchReader(numIterations, batchSize, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, int batchSize, RingBuffer<Event> ringBuffer, Profiler profiler) {
        BatchReader reader = new BatchReader(numIterations, batchSize, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
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
