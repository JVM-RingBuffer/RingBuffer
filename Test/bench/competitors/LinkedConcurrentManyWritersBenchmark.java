package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedConcurrentManyWritersBenchmark extends LinkedConcurrentManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new LinkedConcurrentManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
