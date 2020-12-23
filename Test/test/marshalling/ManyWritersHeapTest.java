package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyWritersHeapTest extends ManyWritersHeapContentionTest {
    public static void main(String[] args) {
        new ManyWritersHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapClearingWriter.runGroupAsync(RING_BUFFER, profiler);
        return HeapClearingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
