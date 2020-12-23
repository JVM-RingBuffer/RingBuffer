package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBlockingBatchContentionTest extends ManyReadersBlockingContentionTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
        return SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
