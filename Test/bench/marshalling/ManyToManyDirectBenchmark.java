package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyToManyDirectBenchmark extends ManyToManyDirectContentionBenchmark {
    public static void main(String[] args) {
        new ManyToManyDirectBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectClearingWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedDirectClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
