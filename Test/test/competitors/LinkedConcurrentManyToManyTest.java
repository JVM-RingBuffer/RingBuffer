package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedConcurrentManyToManyTest extends LinkedConcurrentManyToManyContentionTest {
    public static void main(String[] args) {
        new LinkedConcurrentManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
