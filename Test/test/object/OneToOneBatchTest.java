package test.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBatchTest extends OneToOneTest {
    public static void main(String[] args) {
        new OneToOneBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
