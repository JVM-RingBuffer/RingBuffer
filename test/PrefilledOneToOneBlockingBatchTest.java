package test;

class PrefilledOneToOneBlockingBatchTest extends PrefilledOneToOneBlockingTest {
    public static void main(String[] args) {
        new PrefilledOneToOneBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter writer = PrefilledWriter.startAsync(NUM_ITERATIONS, RING_BUFFER);
        long sum = BatchReader.runAsync(NUM_ITERATIONS, BLOCKING_BATCH_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
