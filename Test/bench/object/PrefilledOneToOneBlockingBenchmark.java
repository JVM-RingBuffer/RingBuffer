package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledOneToOneBlockingBenchmark extends PrefilledOneToOneBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        PrefilledWriter2.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
