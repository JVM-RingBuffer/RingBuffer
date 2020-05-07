package test;

class ManyToManyBatchTest extends ManyToManyTest {
    public static void main(String[] args) {
        new ManyToManyBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup group = Writer.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
