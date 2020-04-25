package test;

class ManyToManyBlockingBatchTest extends ManyToManyBlockingTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = SynchronizedBatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
