package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBatchBenchmark extends PrefilledOneToOneBenchmark {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
