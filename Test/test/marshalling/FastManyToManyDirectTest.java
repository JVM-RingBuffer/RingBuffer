package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class FastManyToManyDirectTest extends FastManyToManyDirectContentionTest {
    public static void main(String[] args) {
        new FastManyToManyDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        FastDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return FastDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
