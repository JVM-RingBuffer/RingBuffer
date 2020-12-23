package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBlockingTest extends PrefilledManyWritersBlockingContentionPerfTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
