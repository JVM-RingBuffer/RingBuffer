package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyToManyDirectBlockingTest extends ManyToManyDirectBlockingContentionPerfTest {
    public static void main(String[] args) {
        new ManyToManyDirectBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
