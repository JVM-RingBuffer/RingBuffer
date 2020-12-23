package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBatchTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
