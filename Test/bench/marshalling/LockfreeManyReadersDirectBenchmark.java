package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyReadersDirectBenchmark extends LockfreeManyReadersDirectContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyReadersDirectBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return LockfreeDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
