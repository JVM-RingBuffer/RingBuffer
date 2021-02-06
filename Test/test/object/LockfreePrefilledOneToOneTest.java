package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreePrefilledOneToOneTest extends LockfreePrefilledOneToOneContentionTest {
    public static void main(String[] args) {
        new LockfreePrefilledOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}