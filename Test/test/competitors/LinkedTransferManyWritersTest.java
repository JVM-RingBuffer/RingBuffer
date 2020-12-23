package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedTransferManyWritersTest extends LinkedTransferManyWritersContentionTest {
    public static void main(String[] args) {
        new LinkedTransferManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
