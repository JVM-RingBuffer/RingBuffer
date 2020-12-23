package test.object;

import eu.menzani.benchmark.Profiler;

public class FastManyReadersTest extends FastManyReadersContentionTest {
    public static void main(String[] args) {
        new FastManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return Reader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
