package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreePrefilledManyToManyTest extends LockfreePrefilledManyToManyContentionTest {
    public static void main(String[] args) {
        new LockfreePrefilledManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreePrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreePrefilledReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
