package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBlockingBatchBenchmark extends PrefilledManyToManyBlockingBenchmark {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
