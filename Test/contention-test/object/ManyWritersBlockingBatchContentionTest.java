package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBlockingBatchContentionTest extends ManyWritersBlockingContentionTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(getRingBuffer(), profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
