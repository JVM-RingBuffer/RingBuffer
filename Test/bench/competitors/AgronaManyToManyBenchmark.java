package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class AgronaManyToManyBenchmark extends AgronaManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new AgronaManyToManyBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(QUEUE, profiler);
        return Reader.runGroupAsync(QUEUE, profiler);
    }
}
