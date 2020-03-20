package perftest;

class PrefilledOneToOneBatchTest extends PrefilledOneToOneTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchTest().runTest();
    }

    @Override
    public long run() {
        BatchReader reader = BatchReader.runAsync(NUM_ITERATIONS, RING_BUFFER);
        PrefilledWriter writer = PrefilledWriter.runAsync(NUM_ITERATIONS, RING_BUFFER);
        reader.reportPerformance();
        writer.reportPerformance();
        return reader.getSum();
    }
}
