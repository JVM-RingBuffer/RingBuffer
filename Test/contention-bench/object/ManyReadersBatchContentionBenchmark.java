package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBatchContentionBenchmark extends ManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new ManyReadersBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
