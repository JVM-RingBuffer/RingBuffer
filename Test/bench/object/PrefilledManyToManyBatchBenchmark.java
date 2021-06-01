package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBatchBenchmark extends PrefilledManyToManyBenchmark {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
