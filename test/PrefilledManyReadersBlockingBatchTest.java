package test;

class PrefilledManyReadersBlockingBatchTest extends PrefilledManyReadersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter writer = PrefilledWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER);
        long sum = SynchronizedAdvancingBatchReader.runGroupAsync(READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
        writer.reportPerformance();
        return sum;
    }
}
