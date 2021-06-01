package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class AgronaOneToOneBenchmark extends AgronaOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new AgronaOneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
