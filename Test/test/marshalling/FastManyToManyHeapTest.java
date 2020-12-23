package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class FastManyToManyHeapTest extends FastManyToManyHeapContentionTest {
    public static void main(String[] args) {
        new FastManyToManyHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return FastHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
