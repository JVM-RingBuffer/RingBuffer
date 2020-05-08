package test;

class ManyToManyBlockingBatchTest extends ManyToManyBlockingTest {
    public static void main(String[] args) {
        new ManyToManyBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runGroupAsync(BLOCKING_BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
