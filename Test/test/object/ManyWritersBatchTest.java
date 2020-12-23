package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBatchTest extends ManyWritersTest {
    public static void main(String[] args) {
        new ManyWritersBatchTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(Holder.RING_BUFFER, profiler);
        return BatchReader.runAsync(TOTAL_ELEMENTS, BATCH_SIZE, Holder.RING_BUFFER, profiler);
    }
}
