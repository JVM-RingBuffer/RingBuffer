package test;

class PrefilledManyToManyBlockingBatchTest extends PrefilledManyToManyBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        TestThreadGroup group = PrefilledWriter.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
