package perftest;

class PrefilledManyWritersBlockingBatchTest extends PrefilledManyWritersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyWritersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledKeyedWriter.runGroupAsync(RING_BUFFER);
        return AdvancingBatchReader.runAsync(TOTAL_ELEMENTS, READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
    }
}
