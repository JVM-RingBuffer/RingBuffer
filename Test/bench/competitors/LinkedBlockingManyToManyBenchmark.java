package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedBlockingManyToManyBenchmark extends LinkedBlockingManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new LinkedBlockingManyToManyBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
