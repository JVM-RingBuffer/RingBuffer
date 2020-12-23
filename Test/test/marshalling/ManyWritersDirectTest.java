package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyWritersDirectTest extends ManyWritersDirectContentionTest {
    public static void main(String[] args) {
        new ManyWritersDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectClearingWriter.runGroupAsync(RING_BUFFER, profiler);
        return DirectClearingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
