package bench.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeOneToOneBenchmark extends LockfreeOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeOneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        LockfreeWriter.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return LockfreeReader.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
    }
}
