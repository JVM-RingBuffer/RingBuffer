package test.competitors;

import eu.menzani.benchmark.Profiler;

public class AgronaManyToManyTest extends AgronaManyToManyContentionTest {
    public static void main(String[] args) {
        new AgronaManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
