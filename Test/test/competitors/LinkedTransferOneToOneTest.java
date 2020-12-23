package test.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedTransferOneToOneTest extends LinkedTransferOneToOneContentionTest {
    public static void main(String[] args) {
        new LinkedTransferOneToOneTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        BlockingWriter.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
