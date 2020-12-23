package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBatchTest extends ManyToManyTest {
    public static void main(String[] args) {
        new ManyToManyBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(Holder.RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
