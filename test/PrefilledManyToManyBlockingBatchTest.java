package test;

class PrefilledManyToManyBlockingBatchTest extends PrefilledManyToManyBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup group = PrefilledSynchronizedWriter.startGroupAsync(RING_BUFFER);
        long sum = SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
