package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class FastManyWritersHeapTest extends FastManyWritersHeapContentionTest {
    public static void main(String[] args) {
        new FastManyWritersHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return FastHeapReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
