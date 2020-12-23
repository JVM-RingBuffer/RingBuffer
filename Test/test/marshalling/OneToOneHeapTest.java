package test.marshalling;

import eu.menzani.benchmark.Profiler;

public class OneToOneHeapTest extends OneToOneHeapContentionTest {
    public static void main(String[] args) {
        new OneToOneHeapTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        HeapClearingWriter.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return HeapClearingReader.runAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
    }
}
