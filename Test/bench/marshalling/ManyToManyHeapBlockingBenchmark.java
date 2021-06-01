package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyToManyHeapBlockingBenchmark extends ManyToManyHeapBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyToManyHeapBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
