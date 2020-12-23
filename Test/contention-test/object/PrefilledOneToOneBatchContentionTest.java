package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBatchContentionTest extends PrefilledOneToOneContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
