package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBlockingBatchTest extends PrefilledManyWritersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.runGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
