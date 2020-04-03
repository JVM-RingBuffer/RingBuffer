package perftest;

class PrefilledManyWritersBlockingBatchTest extends PrefilledManyWritersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        AdvancingBatchReader reader = AdvancingBatchReader.runAsync(TOTAL_ELEMENTS, READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
        TestThreadGroup writerGroup = PrefilledKeyedWriter.runGroupAsync(RING_BUFFER);
        reader.reportPerformance();
        writerGroup.reportPerformance();
        return reader.getSum();
    }
}
