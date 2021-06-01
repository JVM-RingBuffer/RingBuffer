package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedTransferManyToManyBenchmark extends LinkedTransferManyToManyContentionBenchmark {
    public static void main(String[] args) {
        new LinkedTransferManyToManyBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runGroupAsync(QUEUE, profiler);
    }
}
