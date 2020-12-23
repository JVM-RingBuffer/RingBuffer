package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class FastManyReadersDirectTest extends FastManyReadersDirectContentionTest {
    public static void main(String[] args) {
        new FastManyReadersDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastDirectWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return FastDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
