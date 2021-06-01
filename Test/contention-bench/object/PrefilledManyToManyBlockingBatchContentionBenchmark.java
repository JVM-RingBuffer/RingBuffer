package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBlockingBatchContentionBenchmark extends PrefilledManyToManyBlockingContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.startGroupAsync(getRingBuffer(), profiler);
        return SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
