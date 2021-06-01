package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyToManyBlockingBenchmark extends ManyToManyBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyToManyBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(RING_BUFFER, profiler);
        return Reader.runGroupAsync(RING_BUFFER, profiler);
    }
}
