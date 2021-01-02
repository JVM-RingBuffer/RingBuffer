package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeOneToOneDirectTest extends LockfreeOneToOneDirectContentionTest {
    public static void main(String[] args) {
        new LockfreeOneToOneDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeDirectWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return LockfreeDirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
