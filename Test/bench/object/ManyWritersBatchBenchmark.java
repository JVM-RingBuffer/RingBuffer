package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBatchBenchmark extends ManyWritersBenchmark {
    public static void main(String[] args) {
        new ManyWritersBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(Holder.RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
