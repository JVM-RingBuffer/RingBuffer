package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeOneToOneDirectBenchmark extends LockfreeOneToOneDirectContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeOneToOneDirectBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeDirectWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return LockfreeDirectReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
