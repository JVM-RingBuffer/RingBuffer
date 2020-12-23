package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class OneToOneHeapBlockingTest extends OneToOneHeapBlockingContentionPerfTest {
    public static void main(String[] args) {
        new OneToOneHeapBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        HeapWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return HeapReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
