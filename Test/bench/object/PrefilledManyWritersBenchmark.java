package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBenchmark extends PrefilledManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
