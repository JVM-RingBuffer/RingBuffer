package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyWritersHeapTest extends LockfreeManyWritersHeapContentionTest {
    public static void main(String[] args) {
        new LockfreeManyWritersHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreeHeapReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
