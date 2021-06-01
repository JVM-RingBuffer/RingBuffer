package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectClearingRingBuffer;

import static eu.menzani.struct.DirectOffsets.INT;

class DirectClearingReader extends HeapReader {
    static long runGroupAsync(DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new DirectClearingReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectClearingRingBuffer ringBuffer, Profiler profiler) {
        DirectClearingReader reader = new DirectClearingReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private DirectClearingReader(int numIterations, DirectClearingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        DirectClearingRingBuffer ringBuffer = getDirectClearingRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            sum += ringBuffer.readInt(ringBuffer.take(INT));
        }
        return sum;
    }
}
