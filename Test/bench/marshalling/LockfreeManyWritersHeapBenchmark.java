package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyWritersHeapBenchmark extends LockfreeManyWritersHeapContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyWritersHeapBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeHeapWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreeHeapReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
