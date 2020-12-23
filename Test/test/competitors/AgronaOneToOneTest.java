package test.competitors;

import eu.menzani.benchmark.Profiler;

public class AgronaOneToOneTest extends AgronaOneToOneContentionTest {
    public static void main(String[] args) {
        new AgronaOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
