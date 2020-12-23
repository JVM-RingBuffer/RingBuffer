package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedConcurrentOneToOneTest extends LinkedConcurrentOneToOneContentionTest {
    public static void main(String[] args) {
        new LinkedConcurrentOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
