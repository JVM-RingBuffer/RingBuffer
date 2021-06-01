package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBlockingBatchBenchmark extends PrefilledManyReadersBlockingBenchmark {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter2.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
