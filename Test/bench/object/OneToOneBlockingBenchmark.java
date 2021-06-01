package bench.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBlockingBenchmark extends OneToOneBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new OneToOneBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
