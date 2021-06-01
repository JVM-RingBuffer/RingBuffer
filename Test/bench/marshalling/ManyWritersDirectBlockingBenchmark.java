package bench.marshalling;

import eu.menzani.benchmark.Profiler;

public class ManyWritersDirectBlockingBenchmark extends ManyWritersDirectBlockingContentionPerfBenchmark {
    public static void main(String[] args) {
        new ManyWritersDirectBlockingBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        SynchronizedDirectWriter.runGroupAsync(RING_BUFFER, profiler);
        return DirectReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
    }
}
