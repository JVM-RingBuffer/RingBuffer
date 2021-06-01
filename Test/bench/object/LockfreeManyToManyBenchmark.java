package bench.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyToManyBenchmark extends LockfreeManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyToManyBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeWriter.runGroupAsync(Holder.RING_BUFFER, profiler);
        return LockfreeReader.runGroupAsync(Holder.RING_BUFFER, profiler);
    }
}
