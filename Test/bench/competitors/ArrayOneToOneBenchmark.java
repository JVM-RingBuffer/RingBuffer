package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class ArrayOneToOneBenchmark extends ArrayOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new ArrayOneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        BlockingWriter.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
