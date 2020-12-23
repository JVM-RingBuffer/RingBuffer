package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBlockingBatchTest extends ManyWritersBlockingTest {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
