package test;

class PrefilledManyToManyBatchTest extends PrefilledManyToManyTest {
    public static void main(String[] args) {
        new PrefilledManyToManyBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup group = PrefilledWriter.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runGroupAsync(BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
