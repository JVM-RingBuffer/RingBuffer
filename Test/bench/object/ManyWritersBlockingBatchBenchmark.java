package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBlockingBatchBenchmark extends ManyWritersBlockingBenchmark {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
