package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedBlockingManyToManyTest extends LinkedBlockingManyToManyContentionTest {
    public static void main(String[] args) {
        new LinkedBlockingManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
