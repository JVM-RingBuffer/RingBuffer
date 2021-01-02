package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyReadersDirectTest extends LockfreeManyReadersDirectContentionTest {
    public static void main(String[] args) {
        new LockfreeManyReadersDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreeDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
