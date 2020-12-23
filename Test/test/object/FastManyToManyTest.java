package test.object;

import eu.menzani.benchmark.Profiler;

public class FastManyToManyTest extends FastManyToManyContentionTest {
    public static void main(String[] args) {
        new FastManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(Holder.RING_BUFFER, profiler);
        return Reader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
