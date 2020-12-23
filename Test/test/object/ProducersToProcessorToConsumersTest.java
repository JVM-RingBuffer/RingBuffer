package test.object;

import eu.menzani.benchmark.Profiler;

public class ProducersToProcessorToConsumersTest extends ProducersToProcessorToConsumersContentionTest {
    public static void main(String[] args) {
        new ProducersToProcessorToConsumersTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(TOTAL_ELEMENTS);
        Writer.runGroupAsync(PRODUCERS_RING_BUFFER, profiler);
        Processor.runAsync(TOTAL_ELEMENTS, PRODUCERS_RING_BUFFER);
        return Reader.runGroupAsync(CONSUMERS_RING_BUFFER, profiler);
    }
}
