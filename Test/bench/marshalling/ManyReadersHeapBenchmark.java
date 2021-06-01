package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyReadersHeapBenchmark extends ManyReadersHeapContentionBenchmark {
    public static void main(String[] args) {
        new ManyReadersHeapBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        HeapClearingWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedHeapClearingReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
