package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class OneToOneDirectBlockingTest extends OneToOneDirectBlockingContentionPerfTest {
    public static void main(String[] args) {
        new OneToOneDirectBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        DirectWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return DirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
