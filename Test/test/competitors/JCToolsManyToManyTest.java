package test.competitors;

import eu.menzani.benchmark.Profiler;

public class JCToolsManyToManyTest extends JCToolsManyToManyContentionTest {
    public static void main(String[] args) {
        new JCToolsManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
