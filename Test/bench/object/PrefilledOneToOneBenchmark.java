package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBenchmark extends PrefilledOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledOneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
