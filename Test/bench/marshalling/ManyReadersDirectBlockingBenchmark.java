package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyReadersDirectBlockingBenchmark extends ManyReadersDirectBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyReadersDirectBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        DirectWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return SynchronizedDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
