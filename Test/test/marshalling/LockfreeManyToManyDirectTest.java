package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyToManyDirectTest extends LockfreeManyToManyDirectContentionTest {
    public static void main(String[] args) {
        new LockfreeManyToManyDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreeDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
