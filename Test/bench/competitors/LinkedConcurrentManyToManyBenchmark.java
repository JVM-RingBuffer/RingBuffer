package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedConcurrentManyToManyBenchmark extends LinkedConcurrentManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new LinkedConcurrentManyToManyBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
