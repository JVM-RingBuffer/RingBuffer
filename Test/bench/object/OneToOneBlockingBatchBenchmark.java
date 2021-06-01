package bench.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBlockingBatchBenchmark extends OneToOneBlockingBenchmark {
    public static void main(String[] args) {
        new OneToOneBlockingBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
