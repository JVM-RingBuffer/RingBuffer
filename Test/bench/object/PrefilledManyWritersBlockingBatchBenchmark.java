package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBlockingBatchBenchmark extends PrefilledManyWritersBlockingBenchmark {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.runGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
