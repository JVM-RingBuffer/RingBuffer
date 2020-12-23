package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBatchContentionTest extends ManyToManyContentionTest {
    public static void main(String[] args) {
        new ManyToManyBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(Holder.RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
