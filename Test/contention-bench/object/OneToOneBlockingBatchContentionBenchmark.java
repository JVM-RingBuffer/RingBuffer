package bench.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBlockingBatchContentionBenchmark extends OneToOneBlockingContentionBenchmark {
    public static void main(String[] args) {
        new OneToOneBlockingBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, getRingBuffer(), profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, getRingBuffer(), profiler);
    }
}
