package test.object;

import test.Profiler;

class OneToOneBlockingBatchContentionTest extends OneToOneBlockingContentionTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, RING_BUFFER, profiler);
    }
}
