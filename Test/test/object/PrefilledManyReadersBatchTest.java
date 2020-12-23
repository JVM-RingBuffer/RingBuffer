package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBatchTest extends PrefilledManyReadersTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
