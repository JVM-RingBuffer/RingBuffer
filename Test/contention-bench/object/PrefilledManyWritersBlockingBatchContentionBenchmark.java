package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBlockingBatchContentionBenchmark extends PrefilledManyWritersBlockingContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.startGroupAsync(getRingBuffer(), profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
