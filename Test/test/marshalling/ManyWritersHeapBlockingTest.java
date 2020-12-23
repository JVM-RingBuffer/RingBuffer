package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyWritersHeapBlockingTest extends ManyWritersHeapBlockingContentionPerfTest {
    public static void main(String[] args) {
        new ManyWritersHeapBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return HeapReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
