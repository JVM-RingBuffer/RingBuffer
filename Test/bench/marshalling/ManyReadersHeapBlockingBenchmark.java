package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyReadersHeapBlockingBenchmark extends ManyReadersHeapBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyReadersHeapBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        HeapWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
