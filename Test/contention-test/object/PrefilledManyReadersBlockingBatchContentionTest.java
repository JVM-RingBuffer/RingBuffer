package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBlockingBatchContentionTest extends PrefilledManyReadersBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter2.startAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
        return SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
