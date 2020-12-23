package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBatchTest extends ManyReadersTest {
    public static void main(String[] args) {
        new ManyReadersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
