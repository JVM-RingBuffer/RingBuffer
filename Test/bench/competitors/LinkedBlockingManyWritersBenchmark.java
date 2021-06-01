package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedBlockingManyWritersBenchmark extends LinkedBlockingManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new LinkedBlockingManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
