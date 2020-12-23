package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBlockingBatchContentionTest extends PrefilledManyWritersBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.startGroupAsync(getRingBuffer(), profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
