package bench.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;
import org.ringbuffer.marshalling.LockfreeHeapRingBuffer;

public class LockfreeOneToOneHeapContentionBenchmark extends RingBufferBenchmark {
    public static final LockfreeHeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .lockfree()
                    .build();

    public static void main(String[] args) {
        new LockfreeOneToOneHeapContentionBenchmark().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeHeapWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return LockfreeHeapReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
