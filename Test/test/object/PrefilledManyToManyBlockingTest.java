package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBlockingTest extends PrefilledManyToManyBlockingContentionPerfTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
