package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class FastManyWritersDirectTest extends FastManyWritersDirectContentionTest {
    public static void main(String[] args) {
        new FastManyWritersDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return FastDirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
