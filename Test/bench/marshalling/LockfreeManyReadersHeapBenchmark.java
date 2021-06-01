package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyReadersHeapBenchmark extends LockfreeManyReadersHeapContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyReadersHeapBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreeHeapReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
