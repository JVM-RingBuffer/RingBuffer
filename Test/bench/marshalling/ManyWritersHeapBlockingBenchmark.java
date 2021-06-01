package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyWritersHeapBlockingBenchmark extends ManyWritersHeapBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyWritersHeapBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return HeapReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
