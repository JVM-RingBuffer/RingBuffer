package test.object;

import test.Profiler;

class OneToOneBatchContentionTest extends OneToOneContentionTest {
    public static void main(String[] args) {
        new OneToOneBatchContentionTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Profiler profiler = new Profiler(this, NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, RING_BUFFER, profiler);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER, profiler);
    }
}
