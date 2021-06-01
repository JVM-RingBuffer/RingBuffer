package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyReadersDirectBenchmark extends ManyReadersDirectContentionBenchmark {
    public static void main(String[] args) {
        new ManyReadersDirectBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        DirectClearingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedDirectClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
