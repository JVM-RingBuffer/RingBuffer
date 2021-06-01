package bench.object;

import eu.menzani.benchmark.Profiler;

public class OneToOneBatchBenchmark extends OneToOneBenchmark {
    public static void main(String[] args) {
        new OneToOneBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.runAsync(NUM_ITERATIONS, Holder.RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
