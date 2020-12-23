package test.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyTest extends ManyToManyContentionTest {
    public static void main(String[] args) {
        new ManyToManyTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(Holder.RING_BUFFER, profiler);
        return Reader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
