package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBlockingBatchTest extends ManyToManyBlockingTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
