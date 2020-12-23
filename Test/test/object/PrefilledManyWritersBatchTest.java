package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBatchTest extends PrefilledManyWritersTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
