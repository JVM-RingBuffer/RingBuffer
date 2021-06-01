package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyToManyHeapBenchmark extends LockfreeManyToManyHeapContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyToManyHeapBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreeHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
