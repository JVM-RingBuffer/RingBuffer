package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBlockingBatchBenchmark extends PrefilledOneToOneBlockingBenchmark {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter2.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
