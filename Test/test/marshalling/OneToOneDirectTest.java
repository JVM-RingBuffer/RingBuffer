package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class OneToOneDirectTest extends OneToOneDirectContentionTest {
    public static void main(String[] args) {
        new OneToOneDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        DirectClearingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return DirectClearingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
