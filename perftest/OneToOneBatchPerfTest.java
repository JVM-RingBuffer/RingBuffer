package test;

class OneToOneBatchPerfTest extends OneToOnePerfTest {
    public static void main(String[] args) {
        new OneToOneBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
