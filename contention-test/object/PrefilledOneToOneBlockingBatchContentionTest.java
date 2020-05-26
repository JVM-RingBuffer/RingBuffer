package test.object;

import test.Profiler;

class PrefilledOneToOneBlockingBatchContentionTest extends PrefilledOneToOneBlockingContentionTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, RING_BUFFER, profiler);
    }
}
