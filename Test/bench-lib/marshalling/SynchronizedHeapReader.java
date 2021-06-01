package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

import static eu.menzani.struct.HeapOffsets.INT;

class SynchronizedHeapReader extends HeapReader {
    static long runGroupAsync(HeapRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new SynchronizedHeapReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, HeapRingBuffer ringBuffer, Profiler profiler) {
        SynchronizedHeapReader reader = new SynchronizedHeapReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private SynchronizedHeapReader(int numIterations, HeapRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        HeapRingBuffer ringBuffer = getHeapRingBuffer();
        Object readMonitor = ringBuffer.getReadMonitor();
        int data;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            synchronized (readMonitor) {
                int offset = ringBuffer.take(INT);
                data = ringBuffer.readInt(offset);
                ringBuffer.advance(offset + INT);
            }
            sum += data;
        }
        return sum;
    }
}
