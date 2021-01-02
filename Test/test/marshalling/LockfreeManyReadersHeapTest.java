package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyReadersHeapTest extends LockfreeManyReadersHeapContentionTest {
    public static void main(String[] args) {
        new LockfreeManyReadersHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreeHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
