package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyWritersDirectBenchmark extends ManyWritersDirectContentionBenchmark {
    public static void main(String[] args) {
        new ManyWritersDirectBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectClearingWriter.runGroupAsync(RING_BUFFER, profiler);
        return DirectClearingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
