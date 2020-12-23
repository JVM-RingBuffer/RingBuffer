package test.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBatchContentionTest extends PrefilledManyWritersContentionTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
