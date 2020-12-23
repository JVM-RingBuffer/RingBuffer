package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedConcurrentManyReadersTest extends LinkedConcurrentManyReadersContentionTest {
    public static void main(String[] args) {
        new LinkedConcurrentManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
