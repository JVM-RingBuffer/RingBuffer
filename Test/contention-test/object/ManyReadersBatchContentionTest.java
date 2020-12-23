package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBatchContentionTest extends ManyReadersContentionTest {
    public static void main(String[] args) {
        new ManyReadersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
