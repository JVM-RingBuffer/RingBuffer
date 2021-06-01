package bench.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBenchmark extends OneToOneContentionBenchmark {
    public static void main(String[] args) {
        new OneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return Reader.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
    }
}
