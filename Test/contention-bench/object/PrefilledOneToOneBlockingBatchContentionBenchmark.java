package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBlockingBatchContentionBenchmark extends PrefilledOneToOneBlockingContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter2.startAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
