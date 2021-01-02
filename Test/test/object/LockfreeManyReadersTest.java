package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyReadersTest extends LockfreeManyReadersContentionTest {
    public static void main(String[] args) {
        new LockfreeManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return Reader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
