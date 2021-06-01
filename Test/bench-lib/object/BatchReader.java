package bench.object;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import eu.menzani.lang.Numbers;
import org.ringbuffer.object.ObjectRingBuffer;

class BatchReader extends Reader {
    static long runGroupAsync(int batchSize, ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new BatchReader(numIterations, batchSize, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, int batchSize, ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        BatchReader reader = new BatchReader(numIterations, batchSize, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    final int batchSize;

    BatchReader(int numIterations, int batchSize, ObjectRingBuffer<Event> ringBuffer) {
        super(Numbers.ceilDiv(numIterations, batchSize), ringBuffer);
        this.batchSize = batchSize;
    }

    @Override
    long collect() {
        ObjectRingBuffer<Event> ringBuffer = getObjectRingBuffer();
        int batchSize = this.batchSize;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            ringBuffer.takeBatch(batchSize);
            for (int i = batchSize; i > 0; i--) {
                sum += ringBuffer.takePlain().getData();
            }
        }
        return sum;
    }
}
