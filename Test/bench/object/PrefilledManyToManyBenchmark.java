package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBenchmark extends PrefilledManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyToManyBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
