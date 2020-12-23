package test.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBlockingBatchTest extends OneToOneBlockingTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
