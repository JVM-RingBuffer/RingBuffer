package test.competitors;

import eu.menzani.benchmark.Profiler;

public class ArrayManyReadersTest extends ArrayManyReadersContentionTest {
    public static void main(String[] args) {
        new ArrayManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
