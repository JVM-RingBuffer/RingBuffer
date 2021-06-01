package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class ArrayManyWritersBenchmark extends ArrayManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new ArrayManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
