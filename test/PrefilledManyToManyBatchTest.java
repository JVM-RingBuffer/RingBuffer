package test;

class PrefilledManyToManyBatchTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup group = PrefilledSynchronizedWriter.startGroupAsync(RING_BUFFER);
        long sum = SynchronizedBatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
