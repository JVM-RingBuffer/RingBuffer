package bench.object;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.ObjectRingBuffer;

class SynchronizedBatchReader extends BatchReader {
    static long runGroupAsync(int batchSize, ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new SynchronizedBatchReader(numIterations, batchSize, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, int batchSize, ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        SynchronizedBatchReader reader = new SynchronizedBatchReader(numIterations, batchSize, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private SynchronizedBatchReader(int numIterations, int batchSize, ObjectRingBuffer<Event> ringBuffer) {
        super(numIterations, batchSize, ringBuffer);
    }

    @Override
    long collect() {
        ObjectRingBuffer<Event> ringBuffer = getObjectRingBuffer();
        Object readMonitor = ringBuffer.getReadMonitor();
        int batchSize = this.batchSize;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (readMonitor) {
                ringBuffer.takeBatch(batchSize);
                for (int i = batchSize; i > 0; i--) {
                    sum += ringBuffer.takePlain().getData();
                }
            }
        }
        return sum;
    }
}
