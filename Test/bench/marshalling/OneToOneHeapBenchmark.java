package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class OneToOneHeapBenchmark extends OneToOneHeapContentionBenchmark {
    public static void main(String[] args) {
        new OneToOneHeapBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        HeapClearingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return HeapClearingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
