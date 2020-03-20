package perftest;

class PrefilledOneToOneBlockingBatchTest extends PrefilledOneToOneBlockingTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        AdvancingBatchReader reader = AdvancingBatchReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
        PrefilledWriter writer = PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
