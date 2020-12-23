package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyWritersDirectBlockingTest extends ManyWritersDirectBlockingContentionPerfTest {
    public static void main(String[] args) {
        new ManyWritersDirectBlockingTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return DirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
