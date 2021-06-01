package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBlockingBatchBenchmark extends ManyReadersBlockingBenchmark {
    public static void main(String[] args) {
        new ManyReadersBlockingBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER, profiler);
    }
}
