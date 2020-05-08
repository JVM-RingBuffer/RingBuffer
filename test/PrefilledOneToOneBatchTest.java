package test;

class PrefilledOneToOneBatchTest extends PrefilledOneToOneTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBatchTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter writer = PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        long sum = BatchReader.runAsync(NUM_ITERATIONS, BATCH_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
