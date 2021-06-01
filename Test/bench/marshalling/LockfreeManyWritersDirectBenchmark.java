package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyWritersDirectBenchmark extends LockfreeManyWritersDirectContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyWritersDirectBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreeDirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
