package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedTransferManyToManyTest extends LinkedTransferManyToManyContentionTest {
    public static void main(String[] args) {
        new LinkedTransferManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
