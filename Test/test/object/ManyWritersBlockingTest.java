package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBlockingTest extends ManyWritersBlockingContentionPerfTest {
    public static void main(String[] args) {
        new ManyWritersBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
