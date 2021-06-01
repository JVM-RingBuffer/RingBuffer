package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBlockingBatchContentionBenchmark extends ManyReadersBlockingContentionBenchmark {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, getRingBuffer(), profiler);
        return SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
