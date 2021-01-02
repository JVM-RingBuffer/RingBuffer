package test.object;

import eu.menzani.benchmark.Profiler;

public class LockfreePrefilledManyToManyTest extends LockfreePrefilledManyToManyContentionTest {
    public static void main(String[] args) {
        new LockfreePrefilledManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
