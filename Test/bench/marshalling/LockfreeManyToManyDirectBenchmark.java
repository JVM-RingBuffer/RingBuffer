package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyToManyDirectBenchmark extends LockfreeManyToManyDirectContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyToManyDirectBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return LockfreeDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
