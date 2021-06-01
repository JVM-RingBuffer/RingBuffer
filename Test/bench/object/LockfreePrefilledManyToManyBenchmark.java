package bench.object;

import eu.menzani.benchmark.Profiler;

public class LockfreePrefilledManyToManyBenchmark extends LockfreePrefilledManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new LockfreePrefilledManyToManyBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreePrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreePrefilledReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
