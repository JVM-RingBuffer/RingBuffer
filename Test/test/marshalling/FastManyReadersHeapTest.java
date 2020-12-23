package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class FastManyReadersHeapTest extends FastManyReadersHeapContentionTest {
    public static void main(String[] args) {
        new FastManyReadersHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastHeapWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return FastHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
