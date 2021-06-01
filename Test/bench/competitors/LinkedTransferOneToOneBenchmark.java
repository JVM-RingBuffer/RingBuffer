package bench.competitors;

import eu.menzani.benchmark.Profiler;

public class LinkedTransferOneToOneBenchmark extends LinkedTransferOneToOneContentionBenchmark {
    public static void main(String[] args) {
        new LinkedTransferOneToOneBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        BlockingWriter.runAsync(NUM_ITERATIONS, QUEUE, profiler);
        return BlockingReader.runAsync(NUM_ITERATIONS, QUEUE, profiler);
    }
}
