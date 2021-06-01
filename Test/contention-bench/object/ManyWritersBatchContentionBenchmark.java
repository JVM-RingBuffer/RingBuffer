package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBatchContentionBenchmark extends ManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new ManyWritersBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(Holder.RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
