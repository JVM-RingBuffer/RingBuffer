package perftest;

class PrefilledOneToOneBlockingBatchTest extends PrefilledOneToOneBlockingTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        return AdvancingBatchReader.runAsync(NUM_ITERATIONS, READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
    }
}
