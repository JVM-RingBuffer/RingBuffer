package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBlockingBatchTest extends ManyReadersBlockingTest {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
