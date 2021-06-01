package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBatchBenchmark extends PrefilledManyReadersBenchmark {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
