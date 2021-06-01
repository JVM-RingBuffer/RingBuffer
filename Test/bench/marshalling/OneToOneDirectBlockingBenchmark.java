package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class OneToOneDirectBlockingBenchmark extends OneToOneDirectBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new OneToOneDirectBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        DirectWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return DirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
