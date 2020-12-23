package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneTest extends PrefilledOneToOneContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
