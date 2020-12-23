package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersTest extends PrefilledManyWritersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
