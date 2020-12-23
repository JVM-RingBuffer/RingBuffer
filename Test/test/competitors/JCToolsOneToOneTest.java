package test.competitors;

import eu.menzani.benchmark.Profiler;

public class JCToolsOneToOneTest extends JCToolsOneToOneContentionTest {
    public static void main(String[] args) {
        new JCToolsOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
