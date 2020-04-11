package perftest;

class PrefilledManyWritersBlockingBatchTest extends PrefilledManyWritersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        TestThreadGroup group = PrefilledKeyedWriter.startGroupAsync(RING_BUFFER);
        long sum = AdvancingBatchReader.runAsync(TOTAL_ELEMENTS, READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
        group.reportPerformance();
        return sum;
    }
}
