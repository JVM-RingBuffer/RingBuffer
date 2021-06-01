package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedTransferManyWritersBenchmark extends LinkedTransferManyWritersContentionBenchmark {
    public static void main(String[] args) {
        new LinkedTransferManyWritersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        BlockingWriter.runGroupAsync(QUEUE, profiler);
        return BlockingReader.runAsync(TOTAL_ELEMENTS, QUEUE, profiler);
    }
}
