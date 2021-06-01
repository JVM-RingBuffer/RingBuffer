package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedBlockingManyReadersBenchmark extends LinkedBlockingManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new LinkedBlockingManyReadersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
