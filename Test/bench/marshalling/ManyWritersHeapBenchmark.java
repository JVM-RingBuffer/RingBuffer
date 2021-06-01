package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyWritersHeapBenchmark extends ManyWritersHeapContentionBenchmark {
    public static void main(String[] args) {
        new ManyWritersHeapBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapClearingWriter.runGroupAsync(RING_BUFFER, profiler);
        return HeapClearingReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
