package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyToManyDirectTest extends ManyToManyDirectContentionTest {
    public static void main(String[] args) {
        new ManyToManyDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectClearingWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedDirectClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
