package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBlockingTest extends PrefilledManyReadersBlockingContentionPerfTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter2.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
