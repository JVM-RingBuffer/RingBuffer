package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeOneToOneHeapBenchmark extends LockfreeOneToOneHeapContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeOneToOneHeapBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeHeapWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return LockfreeHeapReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
