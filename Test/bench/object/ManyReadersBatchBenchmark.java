package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBatchBenchmark extends ManyReadersBenchmark {
    public static void main(String[] args) {
        new ManyReadersBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
