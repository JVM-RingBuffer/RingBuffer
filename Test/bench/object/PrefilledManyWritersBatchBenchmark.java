package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBatchBenchmark extends PrefilledManyWritersBenchmark {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.runGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
