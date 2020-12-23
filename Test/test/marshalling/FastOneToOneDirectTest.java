package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class FastOneToOneDirectTest extends FastOneToOneDirectContentionTest {
    public static void main(String[] args) {
        new FastOneToOneDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        FastDirectWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return FastDirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
