package bench.object;

import eu.menzani.benchmark.Profiler;

public class LockfreeManyWritersBenchmark extends LockfreeManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new LockfreeManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeWriter.runGroupAsync(Holder.RING_BUFFER, profiler);
        return LockfreeReader.runAsync(TOTAL_ELEMENTS, Holder.RING_BUFFER, profiler);
    }
}
