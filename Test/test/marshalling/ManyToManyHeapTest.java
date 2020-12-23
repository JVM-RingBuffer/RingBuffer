package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyToManyHeapTest extends ManyToManyHeapContentionTest {
    public static void main(String[] args) {
        new ManyToManyHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapClearingWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedHeapClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
