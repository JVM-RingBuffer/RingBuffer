package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyToManyBlockingBenchmark extends PrefilledManyToManyBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
