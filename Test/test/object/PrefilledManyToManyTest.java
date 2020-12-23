package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyTest extends PrefilledManyToManyContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
