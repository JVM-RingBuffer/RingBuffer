package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class JCToolsManyWritersBenchmark extends JCToolsManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new JCToolsManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
