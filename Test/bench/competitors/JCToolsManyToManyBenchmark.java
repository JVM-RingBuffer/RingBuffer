package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class JCToolsManyToManyBenchmark extends JCToolsManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new JCToolsManyToManyBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
