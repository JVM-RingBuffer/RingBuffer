package bench.marshalling;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.DirectRingBuffer;

import static eu.menzani.struct.DirectOffsets.INT;

class DirectReader extends HeapReader {
    static long runGroupAsync(DirectRingBuffer ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new DirectReader(numIterations, ringBuffer));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, DirectRingBuffer ringBuffer, Profiler profiler) {
        DirectReader reader = new DirectReader(numIterations, ringBuffer);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private DirectReader(int numIterations, DirectRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    long collect() {
        DirectRingBuffer ringBuffer = getDirectRingBuffer();
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.take(INT);
            sum += ringBuffer.readInt(offset);
            ringBuffer.advance(offset + INT);
        }
        return sum;
    }
}
