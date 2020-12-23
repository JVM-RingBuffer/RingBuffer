package test.competitors;

import eu.menzani.benchmark.Profiler;

public class ArrayManyToManyTest extends ArrayManyToManyContentionTest {
    public static void main(String[] args) {
        new ArrayManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
