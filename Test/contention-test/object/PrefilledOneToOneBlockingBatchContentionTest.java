package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBlockingBatchContentionTest extends PrefilledOneToOneBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter2.startAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
