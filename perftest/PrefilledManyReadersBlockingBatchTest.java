package perftest;

class PrefilledManyReadersBlockingBatchTest extends PrefilledManyReadersBlockingTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return SynchronizedAdvancingBatchReader.runGroupAsync(READ_BUFFER_BLOCKING_SIZE, RING_BUFFER);
    }
}
