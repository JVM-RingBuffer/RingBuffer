package perftest;

class PrefilledOneToOneBlockingBatchTest extends PrefilledOneToOneBlockingTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter writer = PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        long sum = AdvancingBatchReader.runAsync(NUM_ITERATIONS, READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
