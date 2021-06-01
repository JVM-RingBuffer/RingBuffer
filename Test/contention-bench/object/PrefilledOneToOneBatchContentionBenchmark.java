package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBatchContentionBenchmark extends PrefilledOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
