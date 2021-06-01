package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class JCToolsManyReadersBenchmark extends JCToolsManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new JCToolsManyReadersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
