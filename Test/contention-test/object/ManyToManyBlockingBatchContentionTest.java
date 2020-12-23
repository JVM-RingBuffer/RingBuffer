package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBlockingBatchContentionTest extends ManyToManyBlockingContentionTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(getRingBuffer(), profiler);
        return SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
