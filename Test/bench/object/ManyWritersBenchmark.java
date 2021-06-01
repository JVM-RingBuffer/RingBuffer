package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyWritersBenchmark extends ManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new ManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(Holder.RING_BUFFER, profiler);
        return Reader.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
    }
}
