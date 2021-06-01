package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBlockingBatchContentionBenchmark extends ManyToManyBlockingContentionBenchmark {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(getRingBuffer(), profiler);
        return SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
