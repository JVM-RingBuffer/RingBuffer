package perftest;

class PrefilledManyWritersBlockingBatchTest extends PrefilledManyWritersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        AdvancingBatchReader reader = AdvancingBatchReader.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        TestThreadGroup writerGroup = PrefilledSynchronizedWriter.runGroupAsync(RING_BUFFER);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
