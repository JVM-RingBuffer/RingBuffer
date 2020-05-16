package test;

class PrefilledOneToOneBlockingBatchTest extends PrefilledOneToOneBlockingTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchTest().runBenchmark();
    }

    @Override
    long testSum() {
        PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, RING_BUFFER);
    }
}
