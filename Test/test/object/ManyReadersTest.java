package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersTest extends ManyReadersContentionTest {
    public static void main(String[] args) {
        new ManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return Reader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
