package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBatchContentionBenchmark extends PrefilledManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
