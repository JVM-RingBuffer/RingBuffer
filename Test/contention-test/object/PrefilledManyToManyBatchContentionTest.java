package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBatchContentionTest extends PrefilledManyToManyContentionTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
