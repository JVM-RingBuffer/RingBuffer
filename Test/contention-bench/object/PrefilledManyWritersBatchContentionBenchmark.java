package bench.object;

import eu.menzani.benchmark.Profiler;

public class PrefilledManyWritersBatchContentionBenchmark extends PrefilledManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new PrefilledManyWritersBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedPrefilledWriter.startGroupAsync(RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
