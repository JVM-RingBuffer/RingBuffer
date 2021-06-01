package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class OneToOneDirectBenchmark extends OneToOneDirectContentionBenchmark {
    public static void main(String[] args) {
        new OneToOneDirectBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        DirectClearingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return DirectClearingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
