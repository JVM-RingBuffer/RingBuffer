package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeOneToOneHeapTest extends LockfreeOneToOneHeapContentionTest {
    public static void main(String[] args) {
        new LockfreeOneToOneHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeHeapWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return LockfreeHeapReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
