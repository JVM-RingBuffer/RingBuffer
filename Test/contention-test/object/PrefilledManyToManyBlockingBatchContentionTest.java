package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBlockingBatchContentionTest extends PrefilledManyToManyBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.startGroupAsync(getRingBuffer(), profiler);
        return SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
