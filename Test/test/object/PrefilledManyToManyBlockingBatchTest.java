package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBlockingBatchTest extends PrefilledManyToManyBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
