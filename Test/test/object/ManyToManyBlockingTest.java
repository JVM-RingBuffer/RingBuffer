package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBlockingTest extends ManyToManyBlockingContentionPerfTest {
    public static void main(String[] args) {
        new ManyToManyBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
