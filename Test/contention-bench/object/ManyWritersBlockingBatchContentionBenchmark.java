package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBlockingBatchContentionBenchmark extends ManyWritersBlockingContentionBenchmark {
    public static void main(String[] args) {
        new ManyWritersBlockingBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(getRingBuffer(), profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
