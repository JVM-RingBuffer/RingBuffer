package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedConcurrentManyReadersBenchmark extends LinkedConcurrentManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new LinkedConcurrentManyReadersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
