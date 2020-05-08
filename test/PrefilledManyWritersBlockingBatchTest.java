package test;

class PrefilledManyWritersBlockingBatchTest extends PrefilledManyWritersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().run();
    }

    @Override
    long testSum() {
        TestThreadGroup group = PrefilledWriter.startGroupAsync(RING_BUFFER);
        long sum = BatchReader.runAsync(TOTAL_ELEMENTS, BLOCKING_BATCH_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
