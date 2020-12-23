package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBatchTest extends PrefilledOneToOneTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
