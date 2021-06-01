package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBatchContentionBenchmark extends ManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new ManyToManyBatchContentionBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(Holder.RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
