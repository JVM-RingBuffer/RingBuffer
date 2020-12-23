package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyToManyHeapBlockingTest extends ManyToManyHeapBlockingContentionPerfTest {
    public static void main(String[] args) {
        new ManyToManyHeapBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
