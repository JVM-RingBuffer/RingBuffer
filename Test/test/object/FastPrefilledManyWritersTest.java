package test.object;

import eu.menzani.benchmark.Profiler;

public class FastPrefilledManyWritersTest extends FastPrefilledManyWritersContentionTest {
    public static void main(String[] args) {
        new FastPrefilledManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
