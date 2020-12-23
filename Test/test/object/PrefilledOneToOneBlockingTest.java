package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBlockingTest extends PrefilledOneToOneBlockingContentionPerfTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter2.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
