package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBlockingBatchContentionBenchmark extends PrefilledManyReadersBlockingContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter2.startAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
        return SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
