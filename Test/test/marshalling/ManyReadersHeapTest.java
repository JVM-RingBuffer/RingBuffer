package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyReadersHeapTest extends ManyReadersHeapContentionTest {
    public static void main(String[] args) {
        new ManyReadersHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        HeapClearingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedHeapClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
