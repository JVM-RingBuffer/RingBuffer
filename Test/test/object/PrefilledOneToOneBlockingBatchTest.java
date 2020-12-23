package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBlockingBatchTest extends PrefilledOneToOneBlockingTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter2.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
