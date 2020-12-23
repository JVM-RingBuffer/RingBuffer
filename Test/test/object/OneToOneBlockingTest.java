package test.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBlockingTest extends OneToOneBlockingContentionPerfTest {
    public static void main(String[] args) {
        new OneToOneBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
