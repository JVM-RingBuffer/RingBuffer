package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyWritersDirectTest extends LockfreeManyWritersDirectContentionTest {
    public static void main(String[] args) {
        new LockfreeManyWritersDirectTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreeDirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
