package test.object;

class OneToOneBlockingBatchPerfTest extends OneToOneBlockingPerfTest {
    public static void main(String[] args) {
        new OneToOneBlockingBatchPerfTest().runBenchmark();
    }

    @Override
    protected long testSum() {
        Writer.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
