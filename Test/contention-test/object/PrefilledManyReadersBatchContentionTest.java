package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBatchContentionTest extends PrefilledManyReadersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
