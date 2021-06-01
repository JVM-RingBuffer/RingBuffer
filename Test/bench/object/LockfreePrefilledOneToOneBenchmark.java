package bench.object;

import eu.menzani.benchmark.Profiler;

public class LockfreePrefilledOneToOneBenchmark extends LockfreePrefilledOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new LockfreePrefilledOneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreePrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return LockfreePrefilledReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
