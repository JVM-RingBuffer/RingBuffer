package bench.object;

import eu.menzani.benchmark.Profiler;

public class ProducersToProcessorToConsumersBenchmark extends ProducersToProcessorToConsumersContentionBenchmark {
    public static void main(String[] args) {
        new ProducersToProcessorToConsumersBenchmark().runBenchmark();
    }

    @Override
    protected long measure() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        LockfreeWriter.runGroupAsync(PRODUCERS_RING_BUFFER, profiler);
        Processor.runAsync(TOTAL_ELEMENTS, PRODUCERS_RING_BUFFER);
        return LockfreePrefilledReader.runGroupAsync(CONSUMERS_RING_BUFFER, profiler);
    }
}
