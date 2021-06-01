package bench.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyReadersBenchmark extends LockfreeManyReadersContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyReadersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeWriter.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
        return LockfreeReader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
