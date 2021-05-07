package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeOneToOneTest extends LockfreeOneToOneContentionTest {
    public static void main(String[] args) {
        new LockfreeOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeWriter.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return LockfreeReader.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
    }
}
