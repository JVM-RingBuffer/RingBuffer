package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.LockfreeHeapRingBuffer;

import static eu.menzani.struct.HeapOffsets.INT;

class LockfreeHeapReader extends HeapReader {
    static long runGroupAsync(LockfreeHeapRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new LockfreeHeapReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, LockfreeHeapRingBuffer ringBuffer, Profiler profiler) {
        LockfreeHeapReader reader = new LockfreeHeapReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private LockfreeHeapReader(int numIterations, LockfreeHeapRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        LockfreeHeapRingBuffer ringBuffer = getLockfreeHeapRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.readInt(ringBuffer.take(INT));
        }
        return sum;
    }
}
