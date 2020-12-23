package test.competitors;

import eu.menzani.benchmark.Profiler;

public class JCToolsManyWritersTest extends JCToolsManyWritersContentionTest {
    public static void main(String[] args) {
        new JCToolsManyWritersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
