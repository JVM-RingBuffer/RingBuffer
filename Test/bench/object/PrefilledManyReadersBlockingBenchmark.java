package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBlockingBenchmark extends PrefilledManyReadersBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter2.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
