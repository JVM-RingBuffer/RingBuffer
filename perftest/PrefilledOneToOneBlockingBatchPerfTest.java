package test;

class PrefilledOneToOneBlockingBatchPerfTest extends PrefilledOneToOneBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchPerfTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
