package test.object;

import eu.menzani.benchmark.Profiler;

public class FastPrefilledManyToManyTest extends FastPrefilledManyToManyContentionTest {
    public static void main(String[] args) {
        new FastPrefilledManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
