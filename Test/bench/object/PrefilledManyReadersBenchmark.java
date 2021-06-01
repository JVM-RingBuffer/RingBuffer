package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyReadersBenchmark extends PrefilledManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyReadersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
