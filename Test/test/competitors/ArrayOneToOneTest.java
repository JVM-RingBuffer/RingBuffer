package test.competitors;

import eu.menzani.benchmark.Profiler;

public class ArrayOneToOneTest extends ArrayOneToOneContentionTest {
    public static void main(String[] args) {
        new ArrayOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        BlockingWriter.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
