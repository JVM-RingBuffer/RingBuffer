package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersTest extends PrefilledManyReadersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
