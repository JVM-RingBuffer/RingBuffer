package test.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBatchContentionTest extends OneToOneContentionTest {
    public static void main(String[] args) {
        new OneToOneBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
