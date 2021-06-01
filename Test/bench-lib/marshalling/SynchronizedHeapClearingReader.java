package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapClearingRingBuffer;

import static eu.menzani.struct.HeapOffsets.INT;

class SynchronizedHeapClearingReader extends HeapReader {
    static long runGroupAsync(HeapClearingRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new SynchronizedHeapClearingReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, HeapClearingRingBuffer ringBuffer, Profiler profiler) {
        SynchronizedHeapClearingReader reader = new SynchronizedHeapClearingReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private SynchronizedHeapClearingReader(int numIterations, HeapClearingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        HeapClearingRingBuffer ringBuffer = getHeapClearingRingBuffer();
        Object readMonitor = ringBuffer.getReadMonitor();
        int data;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (readMonitor) {
                data = ringBuffer.readInt(ringBuffer.take(INT));
            }
            sum += data;
        }
        return sum;
    }
}
