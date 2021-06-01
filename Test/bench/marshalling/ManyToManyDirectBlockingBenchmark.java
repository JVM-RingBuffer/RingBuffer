package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyToManyDirectBlockingBenchmark extends ManyToManyDirectBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyToManyDirectBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return SynchronizedDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
