package test;

class PrefilledManyReadersBlockingBatchTest extends PrefilledManyReadersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter writer = PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
