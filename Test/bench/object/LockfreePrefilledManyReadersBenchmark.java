package bench.object;

import eu.menzani.benchmark.Profiler;

public class LockfreePrefilledManyReadersBenchmark extends LockfreePrefilledManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new LockfreePrefilledManyReadersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreePrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreePrefilledReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
