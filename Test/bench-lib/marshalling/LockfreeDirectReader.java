package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.LockfreeDirectRingBuffer;

import static eu.menzani.struct.DirectOffsets.INT;

class LockfreeDirectReader extends HeapReader {
    static long runGroupAsync(LockfreeDirectRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new LockfreeDirectReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, LockfreeDirectRingBuffer ringBuffer, Profiler profiler) {
        LockfreeDirectReader reader = new LockfreeDirectReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private LockfreeDirectReader(int numIterations, LockfreeDirectRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        LockfreeDirectRingBuffer ringBuffer = getLockfreeDirectRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.readInt(ringBuffer.take(INT));
        }
        return sum;
    }
}
