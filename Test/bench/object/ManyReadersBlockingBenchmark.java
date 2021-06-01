package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBlockingBenchmark extends ManyReadersBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyReadersBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
