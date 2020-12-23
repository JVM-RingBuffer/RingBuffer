package test.marshalling;

import eu.menzani.benchmark.Profiler;
import org.ringbuffer.marshalling.HeapRingBuffer;

public class FastOneToOneHeapContentionTest extends RingBufferTest {
    public static final HeapRingBuffer RING_BUFFER =
            HeapRingBuffer.withCapacity(ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .withoutLocks()
                    .build();

    public static void main(String[] args) {
        new FastOneToOneHeapContentionTest().runBenchmark();
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        FastHeapWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return FastHeapReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
