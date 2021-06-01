package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBatchContentionBenchmark extends PrefilledManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyReadersBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
