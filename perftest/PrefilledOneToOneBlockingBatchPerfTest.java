package test;

class PrefilledOneToOneBlockingBatchPerfTest extends PrefilledOneToOneBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
    }
}
