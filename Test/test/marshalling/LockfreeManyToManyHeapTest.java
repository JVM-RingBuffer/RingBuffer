package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyToManyHeapTest extends LockfreeManyToManyHeapContentionTest {
    public static void main(String[] args) {
        new LockfreeManyToManyHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreeHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
