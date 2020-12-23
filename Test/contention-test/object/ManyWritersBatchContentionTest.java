package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBatchContentionTest extends ManyWritersContentionTest {
    public static void main(String[] args) {
        new ManyWritersBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(Holder.RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
