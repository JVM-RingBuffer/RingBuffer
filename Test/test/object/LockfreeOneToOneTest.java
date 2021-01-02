package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeOneToOneTest extends LockfreeOneToOneContentionTest {
    public static void main(String[] args) {
        new LockfreeOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
    }
}
