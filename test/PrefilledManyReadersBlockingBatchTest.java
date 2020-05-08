package test;

class PrefilledManyReadersBlockingBatchTest extends PrefilledManyReadersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        PrefilledWriter writer = PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
