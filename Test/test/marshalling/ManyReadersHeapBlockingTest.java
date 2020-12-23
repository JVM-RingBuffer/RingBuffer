package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyReadersHeapBlockingTest extends ManyReadersHeapBlockingContentionPerfTest {
    public static void main(String[] args) {
        new ManyReadersHeapBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        HeapWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
