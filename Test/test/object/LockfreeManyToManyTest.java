package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyToManyTest extends LockfreeManyToManyContentionTest {
    public static void main(String[] args) {
        new LockfreeManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeWriter.runGroupAsync(Holder.RING_BUFFER, profiler);
        return LockfreeReader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
