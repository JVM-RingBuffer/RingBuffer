package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class FastOneToOneHeapTest extends FastOneToOneHeapContentionTest {
    public static void main(String[] args) {
        new FastOneToOneHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        FastHeapWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return FastHeapReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
