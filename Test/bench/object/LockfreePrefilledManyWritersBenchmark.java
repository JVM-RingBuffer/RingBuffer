package bench.object;

import eu.menzani.benchmark.Profiler;

public class LockfreePrefilledManyWritersBenchmark extends LockfreePrefilledManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new LockfreePrefilledManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreePrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreePrefilledReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
