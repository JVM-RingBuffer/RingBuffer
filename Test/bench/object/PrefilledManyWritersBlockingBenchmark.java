package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBlockingBenchmark extends PrefilledManyWritersBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter2.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
