package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedBlockingManyWritersTest extends LinkedBlockingManyWritersContentionTest {
    public static void main(String[] args) {
        new LinkedBlockingManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
