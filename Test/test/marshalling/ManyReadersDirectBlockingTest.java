package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyReadersDirectBlockingTest extends ManyReadersDirectBlockingContentionPerfTest {
    public static void main(String[] args) {
        new ManyReadersDirectBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        DirectWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
