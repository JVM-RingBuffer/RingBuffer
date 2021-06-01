package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class OneToOneHeapBlockingBenchmark extends OneToOneHeapBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new OneToOneHeapBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        HeapWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return HeapReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
