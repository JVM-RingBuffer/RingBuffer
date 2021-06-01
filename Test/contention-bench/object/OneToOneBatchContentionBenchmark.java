package bench.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBatchContentionBenchmark extends OneToOneContentionBenchmark {
    public static void main(String[] args) {
        new OneToOneBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
