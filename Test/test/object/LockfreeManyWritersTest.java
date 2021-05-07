package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyWritersTest extends LockfreeManyWritersContentionTest {
    public static void main(String[] args) {
        new LockfreeManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeWriter.runGroupAsync(Holder.RING_BUFFER, profiler);
        return LockfreeReader.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
    }
}
