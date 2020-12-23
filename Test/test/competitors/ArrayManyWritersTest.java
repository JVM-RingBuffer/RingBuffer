package test.competitors;

import eu.menzani.benchmark.Profiler;

public class ArrayManyWritersTest extends ArrayManyWritersContentionTest {
    public static void main(String[] args) {
        new ArrayManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
