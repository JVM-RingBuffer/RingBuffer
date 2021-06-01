package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedBlockingOneToOneBenchmark extends LinkedBlockingOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new LinkedBlockingOneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        BlockingWriter.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
