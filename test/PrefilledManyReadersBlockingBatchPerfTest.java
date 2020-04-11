package test;

class PrefilledManyReadersBlockingBatchPerfTest extends PrefilledManyReadersBlockingPerfTest {
    public static void main(String[] args) {
        new PrefilledManyReadersBlockingBatchPerfTest().runTest();
    }

    @Override
    public long run() {
        PrefilledWriter.runAsync(TOTAL_ELEMENTS, RING_BUFFER);
        return SynchronizedAdvancingBatchReader.runGroupAsync(READ_BUFFER_SIZE, RING_BUFFER);
    }
}
