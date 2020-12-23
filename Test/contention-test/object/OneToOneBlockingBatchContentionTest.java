package test.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBlockingBatchContentionTest extends OneToOneBlockingContentionTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
