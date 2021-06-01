package bench.object;

import eu.menzani.benchmark.Profiler;

public class ManyReadersBenchmark extends ManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new ManyReadersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return Reader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
