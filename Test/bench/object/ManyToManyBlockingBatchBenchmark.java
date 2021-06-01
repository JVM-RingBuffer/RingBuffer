package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBlockingBatchBenchmark extends ManyToManyBlockingBenchmark {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
