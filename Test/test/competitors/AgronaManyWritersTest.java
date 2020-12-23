package test.competitors;

import eu.menzani.benchmark.Profiler;

public class AgronaManyWritersTest extends AgronaManyWritersContentionTest {
    public static void main(String[] args) {
        new AgronaManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
