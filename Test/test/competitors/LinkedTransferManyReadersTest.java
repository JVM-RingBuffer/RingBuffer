package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedTransferManyReadersTest extends LinkedTransferManyReadersContentionTest {
    public static void main(String[] args) {
        new LinkedTransferManyReadersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
