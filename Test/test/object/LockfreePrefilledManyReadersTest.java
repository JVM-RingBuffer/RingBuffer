package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreePrefilledManyReadersTest extends LockfreePrefilledManyReadersContentionTest {
    public static void main(String[] args) {
        new LockfreePrefilledManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreePrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreePrefilledReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
