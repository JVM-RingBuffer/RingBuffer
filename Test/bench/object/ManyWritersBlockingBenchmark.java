package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBlockingBenchmark extends ManyWritersBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyWritersBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
