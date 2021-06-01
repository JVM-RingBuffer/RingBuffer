package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyToManyHeapBenchmark extends ManyToManyHeapContentionBenchmark {
    public static void main(String[] args) {
        new ManyToManyHeapBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapClearingWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedHeapClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
