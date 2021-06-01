package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBatchBenchmark extends ManyToManyBenchmark {
    public static void main(String[] args) {
        new ManyToManyBatchBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(Holder.RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
