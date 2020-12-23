package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyReadersDirectTest extends ManyReadersDirectContentionTest {
    public static void main(String[] args) {
        new ManyReadersDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        DirectClearingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedDirectClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
