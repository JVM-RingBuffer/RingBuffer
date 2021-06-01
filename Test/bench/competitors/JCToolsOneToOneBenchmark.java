package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class JCToolsOneToOneBenchmark extends JCToolsOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new JCToolsOneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return Reader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
